package pages;

import elements.Locator;
import elements.WebElement;

import static elements.WebElementsFactory.$;

public class SauceLogin {

    public WebElement username = $(Locator.id("user-name"));
}
