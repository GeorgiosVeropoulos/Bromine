package exceptions;


/**
 * An exception that is usually thrown when a WebElement is no longer accessible in the current DOM.
 * <p>Common reasons for this are:</p>
 * <ul>
 *     <li>DOM updated or changed.</li>
 *     <li>WebPage was refreshed.</li>
 *     <li>WebElement was removed from the current DOM</li>
 * </ul>
 */
public class StaleElementReferenceException extends WebDriverException {


    public StaleElementReferenceException(String s) {
        super(s);
    }
}
