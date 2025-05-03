package client.testng;

import capabilities.Configuration;
import conditions.Be;
import conditions.Have;
import elements.*;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Page;
import sleeper.Sleep;
import testbase.TestBaseTestNG;

import java.time.Duration;

import static elements.WebElementsFactory.$;

@Slf4j
public class TestingWaiters extends TestBaseTestNG {

    public Page page;

    @BeforeMethod(alwaysRun = true)
    public void before() {
        page = new Page();
    }


    @Test(groups = "waitToBe")
    public void test1() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        Configuration.waiters().setTimeout(Duration.ofSeconds(5));
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(5));
//        Assert.assertThrows(TimeOutException.class, () -> {
//            $(Locator.xpath("//a1231321")).waitToBe(Condition.visible);
//        });
        $(Locator.xpath("//a1231321")).waitTo(Be.visible).click();
        boolean exists = page.info.exists();
        log.info("Element exists: " + exists);
    }

    @Test(groups = "waitToBe")
    public void test2() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        Configuration.waiters().setTimeout(Duration.ofSeconds(5));
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(5));
//        $(Locator.xpath("//a1231321")).waitToBe(Condition.visible);
        page.info.waitTo(Be.visible).click();
        log.info("Text to be Info: " + page.info.waitTo(Have.text("Info")).getText());
        Sleep.ForSeconds(3);
        log.info(page.info.getSearchContext().toString());

//        Sleeper.sleep(Duration.ofSeconds(5));
//        elementList.get(0).click();
//        boolean exists = page.info.exists();
//        System.out.println("Element exists: " + exists);
//        boolean exists = $(Locator.xpath("//a1231321")).exists();
//        System.out.println("Element exists: " + exists);
    }
}
