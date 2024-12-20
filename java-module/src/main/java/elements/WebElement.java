package elements;


import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;

//The WElement is the reference of an active
public interface WebElement {

    /**
     * Clicks the WElement (this will mimick a normal user click as described by)
     * @link <a href="https://www.w3.org/TR/webdriver/#dfn-element-click">w3.org.click</a>
     */
    void click();

    void clear();
    void sendKeys(String text);
    String getText();
    String getTagName();

    @CheckReturnValue
    String getAttribute(String attributeName);

    String getProperty(String propertyName);
    boolean isDisplayed();
    boolean isEnabled();

    boolean isSelected();

    /**
     * @return the Locator that was used to define this WebElement.
     */
    Locator getLocator();

    WebElement $(Locator locator);


    /**
     * Will get all child elements of this Web element matching the {@link Locator}
     * !Important! If you are using a locator of type {@linkplain enums.LocatorType#XPATH}
     * make sure to add a <b>.</b> infront of the {@code //} otherwise it will search the entire DOM tree.
     * @param locator the locator we want to use for the children
     * @return a List of WebElements or an empty list if no elements can be found.
     */
    WebElements $$(Locator locator);


    /**
     * The result of searching for this elementwill return the element as fetched by the api
     * "element-1231231" : "elementID -12121212"
     * <p>Can be usefull if you want to create your own API calls to specific elements</p>
     *
     * @return the located elementName:elementId . <p>!!! if the element can't be found in the current dom this will return {@code null}</p>
     */
    @CheckReturnValue
    @CheckForNull
    SearchContext getSearchContext();

    /**
     * Will check if the element is currently present/exists in the DOM
     * @return true if it is. false otherwise.
     */
    boolean exists();
}
