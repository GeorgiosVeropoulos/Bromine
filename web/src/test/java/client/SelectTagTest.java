package client;

import elements.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

@Slf4j
public class SelectTagTest extends TestBase {



    WebElement loginTxt = $(Locator.id("user-name"));

    WebElement passwordTxt = $(Locator.id("password"));

    WebElement loginBtn = $(Locator.id("login-button"));


    WebElement select = $(Locator.xpath("//select [@class = 'product_sort_container']"));

    WebElement noSuchElement = $(Locator.xpath("//adadadada"));



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


    @Test @Tag("TagName")
    public void testClassName() {
        WebDriver.get().open("https://www.georgeveropoulos.com");

//        WebElement element = $(Locator.xpath("//adadadada"));
//        SearchContext exists = element.getSearchContext();

        WebElement element = $(Locator.id("header"));
        Assertions.assertNotNull(element.getTagName());
        System.out.println("exists: " + element.getText());

//        Selenide.$(By.xpath("")).getAttribute()

//        boolean exists = $(Locator.className("actions special")).exists();
//        boolean exists = $(Locator.className(" actions special")).exists();
//        System.out.println(exists);
    }

}
