package client;

import elements.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

@Slf4j
public class SelectTagTest extends TestBase {



    WebElement loginTxt = $(Locator.id("user-name"));

    WebElement passwordTxt = $(Locator.id("password"));

    WebElement loginBtn = $(Locator.id("login-button"));


    WebElement select = $(Locator.xpath("//select [@class = 'product_sort_container']"));



    @Test
    public void selectTest() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");

        loginTxt.sendKeys("standard_user");
        passwordTxt.sendKeys("secret_sauce");
        loginBtn.click();
        loginBtn.getLocator();
        boolean isVisible = new Wait().forResult(ExpectedResult.isVisible(select));
        System.out.println(loginBtn.getLocator().toString());
        WebElements options = new Select(select).getOptions();
        log.info("Testing info message");
        System.out.println("Options size is " + options.size());
//        new Select(select).click();
//        String firstText = new Select(select).getLastChild().getText();
//        System.out.println("First text is: " + firstText);
        System.out.println("isvisible? : " + isVisible);

        System.out.println("Is element a select? : " + select.getTagName());

    }


    @Test
    public void testClassName() {
        WebDriver.get().open("https://www.georgeveropoulos.com");

//        Selenide.$(By.xpath("")).getAttribute()

//        boolean exists = $(Locator.className("actions special")).exists();
//        boolean exists = $(Locator.className(" actions special")).exists();
//        System.out.println(exists);
    }

}
