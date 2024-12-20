package elements;

import java.util.List;

public class WebElementsImpl extends WebElementCollection {
    // Constructor accepting By locator
    public WebElementsImpl(Locator locator) {
        super(locator);
    }

    public WebElementsImpl(List<WebElement> elementList, Locator locator) {
        super(elementList, locator);
    }


}
