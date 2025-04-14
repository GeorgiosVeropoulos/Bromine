package elements;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// This class will handle all method invocations dynamically for WElementList.
public class WElementListInvocationHandler implements InvocationHandler {
    private Locator locator;  // The locator to find the list of elements
    private static final ThreadLocal<WebElementsImpl> elementsThreadLocal = new ThreadLocal<>();
    protected WebElementsImpl fetchedElements;

    public WElementListInvocationHandler(Locator locator) {
        this.locator = locator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // If the real list of elements hasn't been fetched yet, fetch it

        if (method.getDeclaringClass() == Object.class) {
            if (method.getName().equals("toString")) {
                return "Proxy for element located by: " + locator;
            }
            return method.invoke(this, args);  // handle equals, hashCode, etc.
        }

        if (method.getName().equals("getLocator") && args == null) {
            return locator;
        }

        System.out.println("Called invoke for " + locator);
        if (elementsThreadLocal.get() == null || elementsThreadLocal.get().getLocator() != locator) {
            System.out.println("Elements tried to be fetched");
            elementsThreadLocal.set(fetchElementsFromAPI());
            fetchedElements = elementsThreadLocal.get();
        }

        // Delegate the method execution to the real WElementList
        return method.invoke(elementsThreadLocal.get(), args);
    }

    // Fetches the list of elements from the API using the By locator
    private WebElementsImpl fetchElementsFromAPI() {
        return new WebElementsImpl(locator);
    }
}


