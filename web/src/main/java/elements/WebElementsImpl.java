package elements;

import java.util.List;

class WebElementsImpl extends WebElementCollection {
    // Constructor accepting By locator
    protected WebElementsImpl(Locator locator) {
        super(locator);
    }

    protected WebElementsImpl(List<WebElement> elementList, Locator locator) {
        super(elementList, locator);
    }


}
