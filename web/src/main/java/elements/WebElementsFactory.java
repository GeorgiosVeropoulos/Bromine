package elements;


import assertions.Verify;
import lombok.NonNull;

import java.lang.reflect.Proxy;
import java.util.Objects;

public final class WebElementsFactory {

    @SuppressWarnings("unchecked")
    public static WebElement $(@NonNull Locator locator) {
        Objects.requireNonNull(locator, "A locator can't be of null value");
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class},
                new WElementInvocationHandler(locator)
        );
    }

    @SuppressWarnings("unchecked")
    public static WebElements $$(@NonNull Locator locator) {
        Objects.requireNonNull(locator, "A locator can't be of null value");
        return (WebElements) Proxy.newProxyInstance(
                WebElements.class.getClassLoader(),
                new Class<?>[]{WebElements.class},
                new WElementListInvocationHandler(locator)
        );
    }
}
