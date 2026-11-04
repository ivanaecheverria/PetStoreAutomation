# Plan de Implementación — PetStoreAutomation

**Proyecto:** `C:\Users\echev\IdeaProjects\PetStoreAutomation`
**Basado en:** `IA/context_analisis_2026-11-03.md`
**Fecha:** 2026-11-04
**Estado:** Aprobado — pendiente de ejecución por fases

---

## Decisiones tomadas

| # | Pregunta | Decisión |
|---|---|---|
| 1 | Dependencias sin uso | Sin cambios — se quedan como están |
| 2 | Localizadores | Inspeccionados — ver sección de localizadores |
| 3 | URL base en el `.feature` | Se deja como está por ahora |
| 4 | Repositorio Git | Solo local |

---

## Visión general

El plan está organizado en **5 fases ordenadas por dependencia técnica**. Cada fase resuelve un grupo de riesgos y limitaciones identificados en el análisis. Las fases deben ejecutarse en orden, ya que cada una es prerequisito de la siguiente.

```
Fase 1 → Fase 2 → Fase 3 → Fase 4 → Fase 5
Fundamentos  Dependencias  Pasos     Config    Infraestructura
(R1,2,3,6)   (R7,8,9,10)  (R4)      (R11)     (R12,13,14)
```

---

## Fase 1 — Fundamentos de ejecución

**Riesgos que resuelve:** 1 · 2 · 3 · 6

### Problema 1 — Sin runner de pruebas (Riesgos 1 y 2)

El proyecto no tiene ninguna clase anotada con `@Suite` / `@CucumberOptions`, por lo que `gradle test` no puede descubrir los escenarios Cucumber. La ejecución hoy solo funciona desde IntelliJ con `io.cucumber.core.cli.Main`. Tampoco hay configuración de `cucumber.glue` definida.

**Acción:**

Crear una clase runner en `src/test/java/runner/` usando `@Suite` + `@SelectClasspathResource` + `@ConfigurationParameter` (JUnit Platform Suite, que ya está declarada en `build.gradle`). La configuración de `glue` debe apuntar explícitamente a los paquetes `steps` y `hook`.

**Archivos nuevos:** `src/test/java/runner/RunCucumberTest.java`

---

### Problema 2 — Firma incompatible en el paso de tabla (Riesgo 3)

El paso `completa el formulario con los siguientes datos:` recibe una tabla Gherkin (`DataTable`), pero el método Java correspondiente no acepta ningún parámetro:

```java
// Estado actual — incorrecto
@When("completa el formulario con los siguientes datos:")
public void completaElFormularioConLosSiguientesDatos() { ... }
```

Cucumber lanza una excepción al intentar hacer match, independientemente de cualquier otra implementación.

**Acción:**

Agregar el parámetro `io.cucumber.datatable.DataTable` al método:

```java
// Firma corregida
public void completaElFormularioConLosSiguientesDatos(DataTable dataTable) { ... }
```

**Archivos modificados:** `src/test/java/steps/CreateUserStep.java`

---

### Problema 3 — Creación temprana del driver (Riesgo 6)

`CreateUserStep` crea `HomePage` como campo de instancia con inicialización inmediata:

```java
// Estado actual — potencialmente riesgoso
HomePage homePage = new HomePage(DriverManager.getDriver());
```

Esto llama a `DriverManager.getDriver()` al momento de instanciar la clase de pasos, lo cual puede ocurrir **antes** de que el `@Before` del hook haya corrido.

**Acción:**

Mover la inicialización de `homePage` al interior del `@Before` en `Hook.java`, o usar inicialización lazy (se crea solo cuando se usa el primer paso), eliminando el inicializador de campo.

**Archivos modificados:** `src/test/java/steps/CreateUserStep.java` y/o `src/test/java/hook/Hook.java`

---

## Fase 2 — Limpieza de dependencias

**Riesgos que resuelve:** 7 · 8 · 9 · 10

### Problema 4 — Duplicación y conflicto de versiones (Riesgo 7)

Tres dependencias aparecen tanto en `implementation` como en `testImplementation`:

| Dependencia | Configuración actual |
| :--- | :--- |
| `assertj-core:3.23.1` | `implementation` + `testImplementation` |
| `janino:3.1.9` | `implementation` + `testImplementation` |
| `logback-classic:1.4.5` | `implementation` + `testImplementation` |
| `logback-core:1.4.5` | `implementation` + `testImplementation` |

Además, Cucumber tiene versión `7.13.0` en el BOM y `7.18.0` en las declaraciones explícitas.

**Acción:**

- Mover las cuatro dependencias duplicadas exclusivamente a `testImplementation`.
- Unificar la versión de Cucumber: usar solo el BOM `7.18.0` (actualizar el BOM) o declarar las versiones explícitas y eliminar el BOM redundante.
- Mover `cucumber-core` y `cucumber-java` de `implementation` a `testImplementation`.

**Archivos modificados:** `build.gradle`

---

### Problema 5 — Dependencias sin uso (Riesgo 8)

Las siguientes dependencias están declaradas pero no se usan en el código fuente actual. **No se toca nada** — se dejan para uso futuro tal como están.

| Dependencia | Configuración |
| :--- | :--- |
| `testng:7.10.1` | `testImplementation` |
| `rest-assured:5.3.1` | `testImplementation` |
| `allure-junit5:2.23.0` | `testImplementation` |
| `hamcrest:2.2` | `testImplementation` |
| `httpclient:4.5.14` | `implementation` |
| `jackson-databind:2.14.2` | `implementation` |
| `opencsv:5.7.1` | `implementation` |
| `vavr:1.0.0-alpha-4` | `implementation` |

**Acción:** Ninguna.

---

### Problema 6 — Comentario obsoleto en commons-lang3 (Riesgo 9)

El comentario hace referencia a `TestEventDataInitializer.java`, un archivo que no existe:

```groovy
//Library for random string generation in TestEventDataInitializer.java
implementation group: 'org.apache.commons', name: 'commons-lang3', version: '3.0'
```

**Acción:**

Actualizar el comentario para reflejar el uso real (generación de datos de prueba aleatorios) y actualizar la versión de `commons-lang3` a la estable más reciente (`3.14.0`).

**Archivos modificados:** `build.gradle`

---

### Problema 7 — Notación de dependencias obsoleta (Riesgo 10)

El reporte de Gradle (`build/reports/problems/problems-report.html`) indica que la notación `group: 'x', name: 'y', version: 'z'` está deprecada y fallará en Gradle 10.

**Acción:**

Migrar todas las declaraciones en estilo `group: ... name: ... version: ...` a la notación compacta (`'group:name:version'`).

**Archivos modificados:** `build.gradle`

---

## Fase 3 — Implementación de pasos de TC-SIGNUP-01

**Riesgo que resuelve:** 4

Los 9 pasos con `PendingException` deben implementarse usando los localizadores relevados en la siguiente sección.

### Pasos pendientes a implementar

| Paso en el feature | Acción necesaria | Page Object |
| :--- | :--- | :--- |
| `da click en {string}` | Buscar elemento por texto de enlace/botón y hacer click | `HomePage` / `SignInPage` |
| `ingresa a la página de registro` | Verificar que la URL contiene `newAccountForm` o que existe el `<h1>User Information</h1>` | `RegisterPage` |
| `completa el formulario con los siguientes datos:` | Leer la `DataTable`, resolver valores dinámicos (User ID aleatorio, email con timestamp), completar cada campo | `RegisterPage` |
| `deja las preferencias de perfil con sus valores por defecto` | No tocar los selects de preferencias (acción vacía o verificación de estado por defecto) | `RegisterPage` |
| `da click en "Save Account Information"` | Reutiliza el paso `da click en {string}` | — |
| `el sistema redirige a la página de inicio {string}` | Verificar que la URL actual contiene `/catalog` | `HomePage` |
| `la página de inicio muestra el texto {string}` | Verificar presencia del texto en la página | `HomePage` |
| `ingresa el User ID registrado` | Usar el User ID generado en el paso de formulario (estado compartido) | `SignInPage` |
| `ingresa la contraseña registrada` | Usar la contraseña del `DataTable` (estado compartido) | `SignInPage` |
| `envía el formulario de inicio de sesión` | Click en botón submit del login | `SignInPage` |

### Nuevos Page Objects a crear

| Clase | Ruta | Responsabilidad |
| :--- | :--- | :--- |
| `SignInPage` | `src/test/java/page/SignInPage.java` | Interacciones con el formulario de login |
| `RegisterPage` | `src/test/java/page/RegisterPage.java` | Interacciones con el formulario de registro |

### Mecanismo de estado compartido entre pasos

Los datos generados dinámicamente (User ID aleatorio, email con timestamp) deben persistir entre pasos del mismo escenario. Se usará un campo de instancia en `CreateUserStep` para almacenar y recuperar esos valores. `commons-lang3` (`RandomStringUtils`) se usa para generar el User ID.

---

## Localizadores relevados del sitio

> El sitio usa el framework Stripes, que genera `id` dinámicos en cada recarga. Se usa `By.name()` como estrategia principal. Donde no hay `name`, se usa CSS selector.

### Página de catálogo (`/catalog`)

| Elemento | Estrategia | Localizador |
|---|---|---|
| Link "Sign In" | CSS | `#menuContent a[href*='signonForm']` |

### Página de Sign In (`/actions/Account.action?signonForm=`)

| Elemento | Estrategia | Localizador |
|---|---|---|
| Link "Register Now!" | CSS | `a[href*='newAccountForm']` |
| Campo Username | Name | `username` |
| Campo Password | Name | `password` |
| Botón Login | Name | `signon` |

### Página de registro (`/actions/Account.action?newAccountForm=`)

| Elemento | Estrategia | Localizador |
|---|---|---|
| Verificación de página | XPath H1 | `//h1[text()='User Information']` |
| User ID | Name | `username` |
| New password | Name | `password` |
| Repeat password | Name | `repeatedPassword` |
| First name | Name | `account.firstName` |
| Last name | Name | `account.lastName` |
| Email | Name | `account.email` |
| Phone | Name | `account.phone` |
| Address 1 | Name | `account.address1` |
| City | Name | `account.city` |
| State | Name | `account.state` |
| Zip | Name | `account.zip` |
| Country | Name | `account.country` |
| Language preference | Name | `account.languagePreference` |
| Favourite category | Name | `account.favouriteCategoryId` |
| Enable My List | Name | `account.listOption` |
| Enable My Banner | Name | `account.bannerOption` |
| Botón "Save Account Information" | Name | `newAccount` |

### Después del registro — Página de catálogo

| Elemento | Estrategia | Localizador / Verificación |
|---|---|---|
| Verificación de redirección | URL contiene | `/catalog` |
| Texto "docs" visible | Link text | `By.linkText("docs")` |

---

## Fase 4 — Externalización de datos y configuración

**Riesgo que resuelve:** 11

La URL base (`https://petstore.octoperf.com`) está escrita directamente en el Gherkin. **Por decisión del equipo, se deja así por ahora.** Este punto se revisará en una iteración futura si se necesita ejecutar contra otros ambientes.

**Acción:** Ninguna en esta iteración.

---

## Fase 5 — Calidad, seguridad e infraestructura

**Riesgos que resuelve:** 12 · 13 · 14

### Problema 8 — Ruta personal en control de versiones (Riesgo 12)

`.idea/workspace.xml` contiene la ruta `C:/Users/VICTUS/...`, información personal del desarrollador. Este archivo no está en `.gitignore`.

**Acción:**

Agregar `.idea/workspace.xml` al archivo `.gitignore`.

**Archivos modificados:** `.gitignore`

---

### Problema 9 — Sin README (Riesgo 13)

No existe ningún archivo de documentación de instalación, configuración ni ejecución.

**Acción:**

Crear `README.md` en la raíz del proyecto con:
- Descripción del proyecto y stack tecnológico.
- Requisitos previos (Java 21, Chrome, Chromedriver).
- Instrucciones de ejecución (`./gradlew test`).
- Convenciones de naming y estructura de paquetes.

**Archivos nuevos:** `README.md`

---

### Problema 10 — Sin repositorio Git (Riesgo 14)

No se detectó el directorio `.git`. El proyecto no tiene historial de versiones.

**Acción:**

Inicializar un repositorio Git local con `git init` y hacer el primer commit con el estado actual del proyecto, respetando el `.gitignore` existente más la adición de `.idea/workspace.xml`.

> Solo repositorio local. No se crea repositorio remoto.

---

## Resumen de archivos afectados

| Archivo | Acción | Fase |
| :--- | :--- | :--- |
| `build.gradle` | Modificar | 2 |
| `src/test/java/runner/RunCucumberTest.java` | **Crear** | 1 |
| `src/test/java/steps/CreateUserStep.java` | Modificar | 1 y 3 |
| `src/test/java/hook/Hook.java` | Modificar | 1 |
| `src/test/java/page/HomePage.java` | Modificar | 3 |
| `src/test/java/page/SignInPage.java` | **Crear** | 3 |
| `src/test/java/page/RegisterPage.java` | **Crear** | 3 |
| `.gitignore` | Modificar | 5 |
| `README.md` | **Crear** | 5 |

---

## Cobertura que queda fuera de este plan (Riesgo 5)

Los casos TC-SIGNUP-02 a TC-SIGNUP-12 (flujos negativos y de borde) no están incluidos porque:

1. Los escenarios no existen en el feature actual.
2. Implementarlos requiere definir y validar los casos de prueba primero.
3. Son trabajo independiente que depende de que TC-SIGNUP-01 funcione.

Se dejan como tarea futura explícita.

---

## Mantenimiento

Este documento debe actualizarse al completar cada fase. Registrar fecha de inicio, fecha de cierre y cualquier desviación respecto a lo planificado.
