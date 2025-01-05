package client;

import elements.Locator;
import elements.WebElement;
import elements.WebElements;
import elements.WebDriver;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;

public class FindElementsInsideElementTest extends TestBase {


    WebElement nav = $(Locator.xpath("//nav[@id='nav']//ul"));


    @Test
    @Tag("coverage")
    public void test() {
        WebDriver.get().open("https://www.georgeveropoulos.com");

        WebElements li = nav.$$(Locator.tagName("li"));
        WebElement li1 = nav.$(Locator.xpath(".//li"));
        System.out.println(li.size());
        System.out.println(li1.getLocator());
        li1.click();
        System.out.println("Finish");
//        for (WElement e : li) {
//            e.click();
//            Sleeper.sleep(Duration.ofMillis(1_000));
//        }
    }
}
