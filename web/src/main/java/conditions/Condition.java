package conditions;

import elements.Locator;

/**
 * Interface representing a condition that can be applied to a Locator.
 * Implementations of this interface should define the specific condition logic.
 */
public interface Condition {
    boolean apply(Locator locator);
}
