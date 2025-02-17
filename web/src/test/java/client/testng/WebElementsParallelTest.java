package client.testng;

import elements.Locator;
import elements.WebDriver;
import elements.WebElements;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Page;
import testbase.TestBaseTestNG;

import static elements.WebElementsFactory.$$;

public class WebElementsParallelTest extends TestBaseTestNG {

    WebElements elementList = $$(Locator.xpath("//a[contains(@href, '#')]"));


    public Page page;

    @BeforeMethod(alwaysRun = true)
    public void before() {
        page = new Page();
    }


    @Test
    public void listTest() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        System.out.println(0);
        System.out.println(elementList.size());
        elementList.get(0).click();
    }

    @Test
    public void listTest2() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        System.out.println(0);
        System.out.println(elementList.size());
        elementList.get(3).click();
    }
}
