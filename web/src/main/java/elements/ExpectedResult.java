package elements;

import exceptions.NoSuchFrameException;
import java.util.function.Supplier;

public class ExpectedResult {


    public static Supplier<WebDriver> frameAvailableAndSwitchToIt(WebElement frame) {
        return () -> {
            try {
                return WebDriver.get().switchTo().frame(frame);
            } catch (NoSuchFrameException e) {
                return null;
            }
        };
    }

    public static Supplier<WebDriver> frameAvailableAndSwitchToIt(int index) {
        return () -> {
            try {
                return WebDriver.get().switchTo().frame(index);
            } catch (NoSuchFrameException e) {
                return null;
            }
        };
    }

    // Check if element is visible
    public static Supplier<Boolean> isVisible(WebElement element) {
        return () -> {
            try {
                return isPresent(element).get() && element.isDisplayed();
            } catch (RuntimeException e) {
                return false;
            }
        };
    }

    public static Supplier<Boolean> isInvisible(WebElement element) {
        return () -> {
            try {
                return !element.isDisplayed();
            } catch (RuntimeException e) {
                return true;
            }
        };
    }

    // Check if element is clickable (assuming isEnabled means clickable here)
    public static Supplier<Boolean> isClickable(WebElement element) {
        return () -> {
            try {
                return isVisible(element).get() && element.isEnabled();
            } catch (RuntimeException e) {
                return false;
            }
        };
    }

    // Check if element is present (exists in the DOM)
    public static Supplier<Boolean> isPresent(WebElement element) {
        return () -> {
            try {
                return element != null && element.exists(); // Or however presence is defined
            } catch (RuntimeException e) {
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
