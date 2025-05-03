package conditions;

import elements.ExpectedResult;
import elements.Locator;
import elements.Wait;
import exceptions.NoSuchElementException;
import exceptions.StaleElementReferenceException;
import lombok.Getter;

@Getter
public enum Be implements Condition {
    visible {
        @Override
        public boolean apply(Locator locator) {
            return new Wait()
                    .ignoreExceptions(NoSuchElementException.class, StaleElementReferenceException.class)
                    .forResult(ExpectedResult.isVisible(locator));
        }
    },
    enabled {
        @Override
        public boolean apply(Locator locator) {
            return new Wait()
                    .ignoreExceptions(NoSuchElementException.class, StaleElementReferenceException.class)
                    .forResult(ExpectedResult.isClickable(null));
        }
    };
}
