package elements;

import exceptions.NoSuchFrameException;
import exceptions.WebDriverException;
import org.bromine.annotations.ThreadSafe;

import java.util.function.Supplier;

@ThreadSafe
public class ExpectedResult {

    /// ThreadLocal variables to store the current WebElement and last exception
    /// encountered during the execution of the WebDriver commands.
    /// This is used to avoid using the same WebElement in multiple threads
    /// and to keep track of the last exception encountered.
    protected static final ThreadLocal<WebElement> internalWebElement = new ThreadLocal<>();
    protected static final ThreadLocal<WebDriverException> lastException = new ThreadLocal<>();


    public static Supplier<WebDriver> frameAvailableAndSwitchToIt(WebElement frame) {
        return () -> {
            try {
                return WebDriver.get().switchTo().frame(frame);
            } catch (NoSuchFrameException e) {
                lastException.set(e);
                return null;
            }
        };
    }

    public static Supplier<WebDriver> frameAvailableAndSwitchToIt(int index) {
        return () -> {
            try {
                return WebDriver.get().switchTo().frame(index);
            } catch (NoSuchFrameException e) {
                lastException.set(e);
                return null;
            }
        };
    }

    // Check if element is visible
    public static Supplier<Boolean> isVisible(WebElement element) {
        return () -> {
            try {
                return isPresent(element.getLocator()).get() && element.isDisplayed();
            } catch (WebDriverException e) {
                lastException.set(e);
                return false;
            }
        };
    }

    public static Supplier<Boolean> textToBe(Locator locator, String text) {
        return () -> {
            try {
                if (!isPresent(locator).get())
                    return false;
                return internalWebElement.get().getText().equals(text);
            } catch (WebDriverException e) {
                lastException.set(e);
                return false;
            }
        };
    }

    public static Supplier<Boolean> isVisible(Locator locator) {
        return () -> {
            try {
                return isPresent(locator).get() && internalWebElement.get().isDisplayed();
            } catch (WebDriverException e) {
                lastException.set(e);
                return false;
            }
        };
    }

    public static Supplier<Boolean> isInvisible(WebElement element) {
        return () -> {
            try {
                return !element.isDisplayed();
            } catch (WebDriverException e) {
                lastException.set(e);
                return true;
            }
        };
    }

    // Check if element is clickable (assuming isEnabled means clickable here)
    public static Supplier<Boolean> isClickable(WebElement element) {
        return () -> {
            try {
                return isVisible(element).get() && element.isEnabled();
            } catch (WebDriverException e) {
                lastException.set(e);
                return false;
            }
        };
    }

    public static Supplier<Boolean> isPresent(Locator locator) {
        return () -> {
            try {
                internalWebElement.set(new WebElementImpl(DriverClient.findElement(locator), locator));
                return internalWebElement.get() != null && internalWebElement.get().exists();
            } catch (WebDriverException e) {
                lastException.set(e);
                return false;
            }
        };
    }

    // Check if URL contains a specified partial string
    public static Supplier<Boolean> urlContains(String partialUrl) {
        return () -> WebDriver.get().navigation().getCurrentUrl().contains(partialUrl);
    }

    // Example: Check if a page title matches a specific string
    public static Supplier<Boolean> titleIs(String title) {
        return () -> WebDriver.get().getTitle().equals(title);
    }
}
