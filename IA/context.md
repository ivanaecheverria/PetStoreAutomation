# Resumen

El proyecto **PetStoreAutomation** es una solución de automatización de pruebas orientada a la validación de servicios y flujos relacionados con PetStore (API y capacidades E2E). 

Actualmente, el repositorio se encuentra en una fase inicial de inicialización (bootstraping). Se han provisto la estructura base del gestor de compilación Gradle y un conjunto completo de librerías para pruebas BDD (Cucumber), pruebas de API REST (REST Assured, Apache HttpClient), pruebas UI (Selenium WebDriver), aserciones avanzadas (AssertJ, Hamcrest) y reportes de ejecución (Allure Report con JUnit 5). No obstante, el proyecto aún no cuenta con la implementación de Step Definitions, Runners de prueba, Clientes de API, Page Objects ni Modelos de datos (POJOs/DTOs).

- **Ruta base del proyecto:** `C:\Users\echev\IdeaProjects\PetStoreAutomation`
- **Estado del proyecto:** Inicial / En configuración de arquitectura de pruebas.

---

# Arquitectura y módulos

El proyecto adopta la disposición estándar de directorios para proyectos Java basados en Gradle.

```
PetStoreAutomation/
├── .gradle/
├── .idea/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── IA/
│   ├── context.md
│   └── prompt_context.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       └── Main.java
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
│           └── Features/
│               └── CreateUser/
│                   └── CreateUser.feature
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
└── settings.gradle
```

### Módulos y componentes identificados:
- **`src/main/java/org/example/Main.java`**: Archivo generado automáticamente por el arquetipo de IntelliJ IDEA. Contiene un método `main` demostrativo.
- **`src/main/resources/`**: Directorio de recursos de aplicación (actualmente vacío).
- **`src/test/java/`**: Directorio raíz destinado al código fuente de pruebas (Step Definitions, Runners, Hooks, Clientes y Helpers). Actualmente vacío.
- **`src/test/resources/Features/CreateUser/CreateUser.feature`**: Especificación de pruebas en formato Gherkin para la funcionalidad de creación de usuarios. Actualmente sin contenido.
- **`IA/`**: Directorio de soporte y contexto para agentes de asistencia e inteligencia artificial en tareas de ingeniería de pruebas.

---

# Tecnología y versiones

A partir de la inspección del archivo `build.gradle`, `gradle-wrapper.properties` y la configuración de IntelliJ IDEA (`.idea/misc.xml`, `.idea/compiler.xml`), se identifican los siguientes componentes y versiones técnicas:

### Entorno y Herramientas de Construcción
- **Lenguaje:** Java
- **Nivel de lenguaje / Compatibilidad de Bytecode:** JDK 21
- **Herramienta de construcción:** Gradle Wrapper `9.6.0`

### Dependencias y Librerías de Pruebas

| Componente / Librería | Versión declarada | Propósito / Alcance |
| :--- | :--- | :--- |
| **JUnit BOM / Jupiter** | `6.0.0` (BOM) / JUnit Platform | Motor base de ejecución de pruebas (`useJUnitPlatform()`) |
| **Cucumber Core / Java** | `7.18.0` / BOM `7.13.0` | Framework BDD para ejecución de escenarios Gherkin |
| **Cucumber JUnit Platform Engine** | Transmitida vía BOM / Engine | Integración de Cucumber con JUnit 5 Platform |
| **Cucumber JUnit (Legacy)** | `7.18.0` | Runner Cucumber para JUnit 4 |
| **TestNG** | `7.10.1` | Framework de pruebas alternativo |
| **REST Assured** | `5.3.1` | Cliente DSL para pruebas funcionales y validación de APIs REST |
| **Hamcrest** | `2.2` | Matchers y validaciones para REST Assured |
| **Apache HttpClient** | `4.5.14` | Cliente HTTP alternativo de bajo nivel |
| **Selenium Java** | `4.20.0` | Automatización de navegadores web |
| **Jackson Databind** | `2.14.2` | Serialización y deserialización JSON <-> POJO |
| **AssertJ Core** | `3.23.1` | Aserciones fluidas |
| **Allure JUnit 5** | `2.23.0` | Generación de reportes detallados de ejecución |
| **Logback (Classic & Core)** | `1.4.5` | Framework de logging |
| **Janino** | `3.1.9` | Compilador para evaluaciones dinámicas en Logback |
| **OpenCSV** | `5.7.1` | Manejo y lectura de fuentes de datos CSV |
| **Apache Commons Lang3** | `3.0` | Utilidades auxiliares (manipulación de strings, datos aleatorios) |
| **Vavr** | `1.0.0-alpha-4` | Biblioteca para programación funcional en Java |

---

# Contratos (si existen)

- **Contratos de API (OpenAPI / Swagger / WSDL):** No existen especificaciones formales ni archivos de esquema (JSON Schema, YAML) alojados localmente en el repositorio.
- **Contratos de Comportamiento (BDD / Gherkin):** El archivo `src/test/resources/Features/CreateUser/CreateUser.feature` está dispuesto como el contrato de comportamiento funcional para el servicio de usuarios, pero su especificación se encuentra pendiente de redacción.

---

# Módulos de datos

- Actualmente no se encuentran clases de modelo de datos (POJOs, DTOs, Entities o Records) definidas dentro del código fuente (`src/main/java` o `src/test/java`).
- El ecosistema del proyecto cuenta con las dependencias necesarias para soportar modelado de datos fuertemente tipado:
  - `com.fasterxml.jackson.core:jackson-databind:2.14.2` para el mapeo de payloads de solicitud y respuesta en JSON.
  - `com.opencsv:opencsv:5.7.1` para la parametrización de casos de prueba guiados por datos externos en formato CSV.

---

# Estándares y convenciones del código

Con base en los artefactos y configuraciones existentes, se observan las siguientes convenciones:

1. **Gestión de dependencias y construcción:**
   - Sintaxis Gradle Groovy DSL en `build.gradle` y `settings.gradle`.
   - La tarea de test está delegada a la plataforma JUnit: `tasks.test { useJUnitPlatform() }`.
2. **Estructura de características BDD:**
   - Ubicación en `src/test/resources/Features/`.
   - Nomenclatura en **PascalCase** para las subcarpetas y archivos de feature (ejemplo: `Features/CreateUser/CreateUser.feature`).
3. **Paquete base:**
   - Paquete estándar inicial: `org.example`.
4. **Target de compilación:**
   - Estándar Java 21 configurado en el entorno de desarrollo IntelliJ IDEA.

---

# Riesgos, supuestos y limitaciones

### Riesgos y observaciones técnicas en `build.gradle`
1. **Conflicto y divergencia de versiones en Cucumber:**
   - Se declara explícitamente `io.cucumber:cucumber-core:7.18.0` y `io.cucumber:cucumber-java:7.18.0`, pero simultáneamente se importa la plataforma `platform("io.cucumber:cucumber-bom:7.13.0")`. Esta discrepancia puede generar inconsistencias en la resolución transitiva de dependencias del runtime de Cucumber.
   - Se incluye tanto `cucumber-junit:7.18.0` (diseñado para JUnit 4) como `cucumber-junit-platform-engine` (para JUnit 5 Platform), lo que representa una redundancia innecesaria.
2. **Coexistencia de múltiples frameworks de prueba (TestNG vs. JUnit):**
   - El proyecto incluye `org.testng:testng:7.10.1`, mientras que `tasks.test` está configurado exclusivamente para `useJUnitPlatform()` y las dependencias apuntan a JUnit Jupiter 6.0.0 BOM. Si no se separa la ejecución de tareas de Gradle, las pruebas de TestNG no serán ejecutadas o generarán conflictos de classpath.
3. **Duplicación de alcances de dependencias:**
   - Varias dependencias están declaradas de forma redundante en `implementation` y `testImplementation` (por ejemplo: `assertj-core:3.23.1`, `janino:3.1.9`, `logback-classic:1.4.5`, `logback-core:1.4.5`). Siendo un proyecto centrado en automatización de pruebas, deben consolidarse en el alcance adecuado (`testImplementation`).
4. **Versión de Commons Lang obsoleta:**
   - Se utiliza `org.apache.commons:commons-lang3:3.0` (lanzada en 2011), existiendo versiones sustancialmente más recientes y compatibles con Java 21.

### Limitaciones actuales
- **Ausencia de suite ejecutable:** No existen clases Runner anotadas (`@Suite`, `@SelectClasspathResource`) ni clases de Step Definitions mapeadas a las frases de Gherkin.
- **Artefactos vacíos o residuales:**
  - `src/test/resources/Features/CreateUser/CreateUser.feature` no contiene ningún escenario `Scenario` / `Scenario Outline`.
  - `src/main/java/org/example/Main.java` contiene código plantilla por defecto de IntelliJ IDEA ajeno a la arquitectura de automatización de pruebas.
