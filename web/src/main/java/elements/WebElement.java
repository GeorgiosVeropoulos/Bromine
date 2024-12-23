package elements;


import annotations.CanThrow;
import exceptions.ElementClickInterceptedException;
import exceptions.NoSuchElementException;
import exceptions.StaleElementReferenceException;
import exceptions.WebDriverException;
import lombok.SneakyThrows;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;

@CanThrow({
        NoSuchElementException.class,
        StaleElementReferenceException.class,
        WebDriverException.class})
public interface WebElement {

    /**
     * Clicks the WElement (this will mimick a normal user click as described by)
     * @link <a href="https://www.w3.org/TR/webdriver/#dfn-element-click">w3.org.click</a>
     */
    @CanThrow(ElementClickInterceptedException.class)
    void click();

    /**
     * Clears the current value/input on the selected WebElement
     */
    void clear();

    /**
     * Will set the value of a given element
     * The text set will be sent as one without delay between letters and words
     * @param text to be sent.
     */
    void sendKeys(String text);

    /**
     * Gets the visible Text of the WebElement and its children.
     * <p>Example:</p>
     * <pre>{@code
     * <header id="header">
     * 	<h1>Hello</h1>
     * 	<p id="title">World!</p>
     * </header>
     *
     * }</pre>
     * For above WebElement with {@code Locator.id("header")} this method will return {@code "Hello\nWorld!"}
     * @return the text found. or an empty {@code ""} {@link String}
     */
    String getText();

    /**
     * Gets the name of the Tag of the current WebElement
     * <p> Example: </p>
     * <pre>
     *     {@code The tag <div> will return div}
     * </pre>
     * @return the tag name.
     */
    String getTagName();

    /**
     * Gets the selected attribute
     * <p> Example: </p>
     * <pre>
     *     {@code for <div exampleattribute='hello world!'>}
     * </pre>
     * From the above code {@code getAttribute("exampleattribute") will return "hello world!"}
     * @param attributeName we want to fetch
     * @return the result of the search or empty string
     */
    @CheckReturnValue
    String getAttribute(String attributeName);

    String getProperty(String propertyName);

    /**
     *
     * @return true if element is visible / false if it isn't
     */
    boolean isDisplayed();


    boolean isEnabled();

    /**
     * Useful to check input elements that can be checked.
     * @return true if selected/checked or false if not.
     */
    boolean isSelected();

    /**
     * @return the Locator that was used to define this WebElement.
     */
    Locator getLocator();

    /**
     * Find an element that is a children of the current element.
     * @param locator to be used to find the element.
     * @return the element if it exists.
     */
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
     * @return true if it is. false if it doesn't or any {@link WebDriverException} happens
     */
    boolean exists();
}
