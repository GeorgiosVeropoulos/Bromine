package elements;

import java.util.Map;

/**
 * The search context of an element.
 * The result of trying to find an element: https://www.w3.org/TR/webdriver/#find-element
 */
public record SearchContext(String elementName, String elementId) {

    public SearchContext(Map<String, String> element) {
        this(
            element.keySet().iterator().next(),
            element.values().iterator().next()
        );
    }
}
