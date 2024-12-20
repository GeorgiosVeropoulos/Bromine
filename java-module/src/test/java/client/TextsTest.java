package client;

import elements.*;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

public class TextsTest extends TestBase {



    WebElement header = $(Locator.id("header"));
    WebElement gitHubIcon = $(Locator.xpath("(//ul[@class='icons']//li)[1]"));

    WebElement username = $(Locator.id("user-name"));

    @Test
    public void getTextTest() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        System.out.println("Text fetched was: " + header.getText());
        System.out.println("GitHubIcon Text fetched was: " + gitHubIcon.getText());
    }

    @Test
    public void test1() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");
//        ActionTemplate template = new ActionTemplate();
        WebElement element = null;
//        new Actions().createAction();
//        String json = template.jsonToSend(username);
//        new Actions().perform(json);
//        new Actions().perform(new Actions().createAction(username));
        System.out.println("Finsih");

    }
}
