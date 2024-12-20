package client;

import elements.Locator;
import elements.WebElement;
import elements.WebElements;
import elements.WebDriver;
import org.junit.jupiter.api.Test;
import testbase.TestBase;

import static elements.WebElementsFactory.$;
import static elements.WebElementsFactory.$$;

public class newTest extends TestBase {

    WebElement aboutMe = $(Locator.xpath("//*[@id='about-me']"));
    WebElement myWork = $(Locator.xpath("//*[@id='my-work']"));
    WebElement workedAt = $(Locator.xpath("//*[@id='worked-at']"));
    WebElement info = $(Locator.xpath("//*[@id='info']"));


    WebElement element2 = $(Locator.xpath("//*[@id='adwqda']"));
    WebElement element3 = $(Locator.xpath("//*[@id='adwqda']"));
    WebElements elementList = $$(Locator.xpath("//a[contains(@href, '#')]"));
    WebElement cvBtn = $(Locator.xpath("//a[contains(@href, 'images')]"));

    @Test
    public void test() {

        WebDriver.get().open("https://www.georgeveropoulos.com");
//        DriverClient.setImplicitWait(Duration.ofMillis(5000));
//        String id = DriverClient.sessionId();
//        List<String> elementIds = DriverClient.getElements("xpath", "adadwqeqeq");
//        int size = elementList.size();
//        System.out.println(size);
//        System.out.println(elementIds.isEmpty());

        boolean isDisplayed = info.isDisplayed();
        System.out.println("Property for baseURI: " + info.getProperty("baseURI"));
        System.out.println(elementList.size());
//
////        elementList.get(3).click();
//        info.click();
//        Sleeper.sleep(TWO_SECONDS);
////        elementList.get(0).click();
//        aboutMe.click();
//        Sleeper.sleep(TWO_SECONDS);
////        elementList.get(2).click();
//        workedAt.click();
//        Sleeper.sleep(TWO_SECONDS);
////        elementList.get(1).click();
//        myWork.click();
//        Sleeper.sleep(TWO_SECONDS);
//        System.out.println("");
//        Sleeper.sleep(TWO_SECONDS);



//        String handle = DriverClient.Contexts.getWindowHandle();
////        DriverClient.Contexts.close();
//        DriverClient.Contexts.switchToWindow(handle);
////        cvBtn.click();
//        List<String> handles = DriverClient.Contexts.getWindowHandles();
//        DriverClient.Contexts.switchToWindow("1112312321");
//        DriverClient.Contexts.newTab();
//        Sleeper.sleep(TWO_SECONDS);
//        DriverClient.Contexts.closeWindow();
//        Sleeper.sleep(TWO_SECONDS);
//        DriverClient.Contexts.switchToWindow(handles.get(0));
//        Sleeper.sleep(TWO_SECONDS);
//        System.out.println("");



    }
}
