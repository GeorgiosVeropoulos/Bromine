package pages;

import elements.Locator;
import elements.WebElement;
import elements.WebElements;

import static elements.WebElementsFactory.$;
import static elements.WebElementsFactory.$$;

public class Page {


    public WebElements elementList = $$(Locator.xpath("//a[contains(@href, '#')]"));


    public WebElement myWork = $(Locator.xpath("(//a[contains(@href, '#')])[2]"));

    public WebElement aboutMe = $(Locator.xpath("(//a[contains(@href, '#')])[1]"));

    public WebElement info = $(Locator.xpath("//*[@id='info']"));
}
