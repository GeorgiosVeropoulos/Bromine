package elements;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// This class will handle all method invocations dynamically for WElementList.
public class WElementListInvocationHandler implements InvocationHandler {
    private Locator locator;  // The locator to find the list of elements
    private static final ThreadLocal<WebElements> elementsThreadLocal = new ThreadLocal<>();

    public WElementListInvocationHandler(Locator locator) {
        this.locator = locator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // If the real list of elements hasn't been fetched yet, fetch it


        System.out.println("Called invoke for " + locator);
        if (elementsThreadLocal.get() == null) {
            elementsThreadLocal.set(fetchElementsFromAPI());
        }

        // Delegate the method execution to the real WElementList
        return method.invoke(elementsThreadLocal.get(), args);
    }

    // Fetches the list of elements from the API using the By locator
    private WebElements fetchElementsFromAPI() {
        return new WebElementsImpl(locator);
    }
}


