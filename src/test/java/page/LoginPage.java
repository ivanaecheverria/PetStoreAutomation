package page;

import interfaces.ButtonClick;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

//Constructor inicializa la clase y da valor a los objetos
public class LoginPage extends BasePage implements ButtonClick {

    private By registerNowButton = By.xpath("//*[@id=\"Catalog\"]/a");


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void darClickRegisterNow(){

    }

    public void darClick() {
        click(registerNowButton);
    }
}
