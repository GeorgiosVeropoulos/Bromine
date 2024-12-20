package elements;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.Proxy;
import java.util.Objects;

public final class WebElementsFactory {

    private WebElementsFactory() {
        throw new IllegalArgumentException("WElementFactory should NOT be instantiated!");
    }


    @CheckReturnValue
    @Nonnull
    @SuppressWarnings("unchecked")
    public static WebElement $(Locator locator) {
        Objects.requireNonNull(locator, "A locator can't be of null value");
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class<?>[]{WebElement.class},
                new WElementInvocationHandler(locator)
        );
    }

    @CheckReturnValue
    @Nonnull
    @SuppressWarnings("unchecked")
    public static WebElements $$(Locator locator) {
        Objects.requireNonNull(locator);
        return (WebElements) Proxy.newProxyInstance(
                WebElements.class.getClassLoader(),
                new Class<?>[]{WebElements.class},
                new WElementListInvocationHandler(locator)
        );
    }
}
