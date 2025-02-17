package elements;

import exceptions.NoSuchElementException;
import exceptions.WebDriverException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// This class will handle all method invocations dynamically.
public class WElementInvocationHandler implements InvocationHandler {
    private Locator locator;  // The locator to find the element
//    private WebElement realElement; // The actual WElement

    // this was added to make sure parallel tests work properly in TestNG when multiple Test methods exist under the same Test class.
    private static final ThreadLocal<WebElement> threadLocalElement = new ThreadLocal<>();

    public WElementInvocationHandler(Locator locator) {
        this.locator = locator;
    }

    // This method will be called for every method invocation on the proxy object.
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Ignore some common Object methods (e.g., toString, equals, hashCode) to prevent early initialization
        if (method.getDeclaringClass() == Object.class) {
            if (method.getName().equals("toString")) {
                return "Proxy for element located by: " + locator;
            }
            return method.invoke(this, args);  // handle equals, hashCode, etc.
        }

        if (method.getName().equals("getBy") && args == null) {
            return locator;
        }

        if (method.getName().equals("exists") && args == null)  {
            try {
                System.out.println("was this called?");
                threadLocalElement.set(fetchElementFromAPI());
            } catch (WebDriverException e) {
                return Boolean.FALSE;
            }
        }

        if (method.getName().equals("getSearchContext") && args == null)  {
            try {
                System.out.println("invoke getSearchContext this called?");
                return DriverClient.getElement(locator);
            } catch (WebDriverException e) {
                return null;
            }
        }

        // If realElement is null, fetch it when a real method on WebElement is called
        if (threadLocalElement.get() == null) {
            System.out.println("Lazy initialization: Fetching real element for " + locator);
            threadLocalElement.set(fetchElementFromAPI());
        }

        try {
            return method.invoke(threadLocalElement.get(), args);
        } catch (Throwable e) {
            // Ensure correct exception propagation
            throw e.getCause();
        }
    }

    // Fetches the element from the API using the By locator
    private WebElement fetchElementFromAPI() throws NoSuchElementException {
        return new WebElementImpl(DriverClient.getElement(locator), locator); // Return the real element
    }
}


