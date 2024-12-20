package elements;

import exceptions.NoSuchElementException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// This class will handle all method invocations dynamically.
public class WElementInvocationHandler implements InvocationHandler {
    private Locator locator;  // The locator to find the element
    private WebElement realElement; // The actual WElement

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

        // If realElement is null, fetch it when a real method on WebElement is called
        if (realElement == null) {
            System.out.println("Lazy initialization: Fetching real element for " + locator);
            realElement = fetchElementFromAPI();
        }

        // Dynamically check annotations using the stack trace
//        Field field = findAnnotatedFieldFromStackTrace(proxy);
//        if (field != null) {
//            for (Annotation annotation : field.getDeclaredAnnotations()) {
//                if (annotation.annotationType() == Interactable.class) {
//                    System.out.println("Ensuring element is interactable for field: " + field.getName());
//                    if (!realElement.isEnabled()) {
//                        throw new IllegalStateException("Element is not enabled for interaction: " + by);
//                    }
//                }
//            }
//        }

        // Delegate method execution to realElement
        try {
            return method.invoke(realElement, args);
        } catch (Throwable e) {
            // Ensure correct exception propagation
            throw e.getCause();
        }
    }

    private Field findAnnotatedFieldFromStackTrace(Object proxy) {
        try {
            // Get the current thread's stack trace
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (StackTraceElement element : stackTrace) {
                String className = element.getClassName();

                // Skip irrelevant classes (like JDK/internal libraries)
                if (className.startsWith("java.") || className.startsWith("sun.")) {
                    continue;
                }

                Class<?> callerClass = Class.forName(className);
                System.out.println(callerClass.toString());
                // Iterate through fields in the caller class
//                for (Field field : callerClass.getDeclaredFields()) {
//                    field.setAccessible(true); // Make private fields accessible
//
//                    // Check if the proxy matches the field value
//                    if (field.getType().isAssignableFrom(proxy.getClass())) {
//                        Object fieldValue = field.get(null); // Static access
//                        if (fieldValue == proxy) {
//                            return field; // Return the matching field
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to find the field for proxy in static context", e);
        }
        return null; // No matching field found
    }
    // Fetches the element from the API using the By locator
    private WebElement fetchElementFromAPI() throws NoSuchElementException {
        return new WebElementImpl(DriverClient.getElement(locator), locator); // Return the real element
    }
}


