package conditions;

import elements.ExpectedResult;
import elements.Locator;
import elements.Wait;
import exceptions.NoSuchElementException;
import exceptions.StaleElementReferenceException;

//@Getter
public class Have {

    public static Condition text(String expectedText) {
        return new Condition() {
            @Override
            public boolean apply(Locator locator) {
                return new Wait()
                        .ignoreExceptions(NoSuchElementException.class, StaleElementReferenceException.class)
                        .forResult(ExpectedResult.textToBe(locator, expectedText));
            }

            @Override
            public String toString() {
                return "Have.text(\"" + expectedText + "\")";
            }
        };
    }

    // Similarly for value, css, etc.
}
