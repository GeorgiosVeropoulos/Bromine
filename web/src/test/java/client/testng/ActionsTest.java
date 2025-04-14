package client.testng;

import elements.Actions;
import elements.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Page;
import pages.SauceLogin;
import testbase.TestBaseTestNG;

public class ActionsTest extends TestBaseTestNG {


    private Page page;
    private SauceLogin sauceLogin;


    @BeforeMethod
    public void before() {
        page = new Page();
        sauceLogin = new SauceLogin();
    }


    @Test
    public void actionTest() {
        WebDriver.get().open("https://www.georgeveropoulos.com");

//        new Actions(page.navBarLis.get(2)).contextClick().build();
        System.out.println("");
        new Actions(page.navBarLis.get(3)).click().build();
        System.out.println("");
        new Actions(page.navBarLis.get(0)).click().build();
//        System.out.println("");
    }

    @Test
    public void actionTest2() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");

//        new Actions(page.navBarLis.get(2)).contextClick().build();
        System.out.println("");
//        new org.openqa.selenium.interactions.Actions(null).scrollByAmount(0, 0).click(null)
        new Actions(sauceLogin.loginBtn).click().build();
        System.out.println("");
//        System.out.println("");
    }


}
