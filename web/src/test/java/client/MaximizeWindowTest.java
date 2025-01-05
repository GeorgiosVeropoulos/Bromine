package client;

import elements.*;
import exceptions.NoSuchElementException;
import exceptions.TimeOutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.Page;
import sleeper.Sleeper;
import testbase.TestBase;

import java.time.Duration;

import static elements.WebElementsFactory.$;
import static elements.WebElementsFactory.$$;

public class MaximizeWindowTest extends TestBase {



    WebElement element = $(Locator.xpath("//adasdadqwe"));
    WebElements elementList = $$(Locator.xpath("//a[contains(@href, '#')]"));


    public Page page;

    @BeforeEach
    public void before() {
        page = new Page();
    }


    @Test
    public void listTest() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        System.out.println(0);
        System.out.println(elementList.size());
        System.out.println(1);
    }

    @Test @Tag("parallel") @Tag("coverage")
    public void maximizeWindowTest() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        System.out.println(elementList.size());

        long start = System.currentTimeMillis();
        Wait wait = new Wait(Duration.ofMillis(10_000L), Duration.ofMillis(500))
                .ignoreExceptions(NoSuchElementException.class, TimeOutException.class);
        boolean isVisible = wait.forResult(ExpectedResult.isVisible(element));
        System.out.println("isVisible: " + isVisible);
//        System.out.println("Title is : " + wait.waitForCondition(ExpectedResult.frameAvailable(element)));
//        wait.waitForCondition(ExpectedResult.isClickable(element));
//        long finish = System.currentTimeMillis();
        ;
//        System.out.println(selenide.size());
        //{"value":[
        // {"element-6066-11e4-a52e-4f735466cecf":"f.35E23EC7D7ACD73C3494E894A1071EC9.d.F7B04281A4E8BE45F7CBB492D4C11784.e.30"},
        // {"element-6066-11e4-a52e-4f735466cecf":"f.35E23EC7D7ACD73C3494E894A1071EC9.d.F7B04281A4E8BE45F7CBB492D4C11784.e.29"},
        // {"element-6066-11e4-a52e-4f735466cecf":"f.35E23EC7D7ACD73C3494E894A1071EC9.d.F7B04281A4E8BE45F7CBB492D4C11784.e.31"},
        // {"element-6066-11e4-a52e-4f735466cecf":"f.35E23EC7D7ACD73C3494E894A1071EC9.d.F7B04281A4E8BE45F7CBB492D4C11784.e.32"}]}
        WebDriver.get().open("https://www.google.com");
//        page.myWork.click();
//        System.out.println(elements.size());
//        for (WElement element : elements) {
//            System.out.println(element.getElementId());
//        }
//        System.out.println(elementList.size());
//        page.aboutMe.click();
//        WebDriver.get().window().maximize();
        Sleeper.sleep(Duration.ofSeconds(1));
    }
}
