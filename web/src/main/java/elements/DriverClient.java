package elements;

import capabilities.Configuration;
import exceptions.NoSuchElementException;
import exceptions.NoSuchFrameException;
import exceptions.NoSuchWindowException;
import json.JsonBuilder;
import json.JsonParser;
import sleeper.Sleeper;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

import static Constants.Constants.TWO_SECONDS;
import static elements.EndPoints.buildEndpoint;
import static elements.HttpMethodExecutor.*;


/**
 * Client class that all HTTP request happen.
 */
abstract class DriverClient {
     // URL where the WebDriver is running
    protected static final ThreadLocal<String> SESSION_IDS = new ThreadLocal<>();
    private static final Logger logger = Logger.getLogger(DriverClient.class.getName());


    protected static String sessionId() {
        return SESSION_IDS.get();
    }




    // Start a browser session
    protected static void startSession() {
        Response response = START(Configuration.getJsonConfig());
        String sessionID = (String) JsonParser.findValueByKey(response, "sessionId");
        if (sessionID != null) {
            SESSION_IDS.set(sessionID);
            return;
        }
//        for (int i=0; i < 5; i++) {
//            Sleeper.sleep(TWO_SECONDS);
//            response = START(Configuration.getJsonConfig());
//            sessionID = (String) JsonParser.findValueByKey(response, "sessionId");
//            if (sessionID != null) {
//                break;
//            }
//        }

        throw new RuntimeException("sessionID wasn't able to be set, Could this be a hub issue?");
    }

    protected static void openURL(String url) {
        doPostRequest(EndPoints.URL, new JsonBuilder().addKeyValue("url", url).build());
    }

    protected static void maximize() {
        doPostRequest(EndPoints.MAXIMIZE_WINDOW, "{}");
    }

    protected static void fullscreen() {
        doPostRequest(EndPoints.FULLSCREEN_WINDOW, "{}");
    }

    protected static void minimize() {
        doPostRequest(EndPoints.MINIMIZE_WINDOW, "{}");
    }

    protected static String getUrl() {
        return (String) doGetRequest(EndPoints.URL).get("value");
    }

    // Close the browser session
    protected static void closeSession() {
        if (sessionId() == null) {
            logger.warning("Tried to close session but session was already closed!");
            return;
        }
        doDeleteRequest("");
        SESSION_IDS.remove();
    }

    protected static SearchContext getElement(Locator locator) {
        String jsonToSend =  new JsonBuilder().addKeyValue("using", locator.getUsing()).addKeyValue("value", locator.getValue()).build();
        Response response = doPostRequest(EndPoints.FIND_ELEMENT , jsonToSend);
        HandleExceptions.handleResponse(response, "Element Not found using: " + locator.toString());
        return new SearchContext(Collections.unmodifiableMap(response.getValueAsMap()));
    }


    protected static List<WebElement> getElements(Locator locator) {
        String jsonToSend = new JsonBuilder().addKeyValue("using", locator.getUsing()).addKeyValue("value", locator.getValue()).build();
        Map<String, Object> json = doPostRequest(EndPoints.FIND_ELEMENTS, jsonToSend);
         ArrayList<HashMap<String, String>> valueMap = (ArrayList<HashMap<String, String>>) json.get("value");

        List<WebElement> elements = new ArrayList<>(); // Temporary list to store fetched elements
        for (HashMap<String, String> stringStringMap : valueMap) {
            elements.add(new WebElementImpl(new SearchContext(stringStringMap), locator));
        }
        return elements;
    }

    protected static SearchContext getElementWithin(String parentId, Locator locator) {
        String endPoint = buildEndpoint(EndPoints.FIND_ELEMENT_FROM_ELEMENT, parentId);
        Response response = doPostRequest(endPoint, new JsonBuilder().addKeyValue("using", locator.getUsing()).addKeyValue("value", locator.getValue()).build());
        HandleExceptions.handleResponse(response, "Could not find element: " + locator.toString());
        return new SearchContext(response.getValueAsMap());
    }

    // Retrieves multiple child elements within a parent element's context
    protected static List<WebElement> getElementsWithin(String parentId, Locator locator) {
        String endPoint = buildEndpoint(EndPoints.FIND_ELEMENTS_FROM_ELEMENT, parentId);
        Response response = doPostRequest(endPoint, new JsonBuilder().addKeyValue("using", locator.getUsing()).addKeyValue("value", locator.getValue()).build());
        // Parse the response to get element IDs
        ArrayList<Map<String, String>> valueMap = (ArrayList<Map<String, String>>) response.get("value");
        // Create a list to hold WElement objects
        List<WebElement> elements = new LinkedList<>();
        // Iterate over the valueMap and convert each element ID to a WElement
        for (Map<String, String> stringStringMap : valueMap) {
//            for (String elementId : stringStringMap.values()) {
//                elements.add(new WElementImpl(elementId, by));  // Create WElement with elementId and By locator
//            }
            elements.add(new WebElementImpl(new SearchContext(stringStringMap), locator));
        }

        return elements;
    }


    protected static void setImplicitWait(Duration duration) {
        String jsonToSend = new JsonBuilder().addKeyValue("implicit", duration.toMillis()).build();
        doPostRequest(EndPoints.TIMEOUTS, jsonToSend);
    }

    protected static Duration getImplicitWait() {
        Map<String, Object> json = doGetRequest(EndPoints.TIMEOUTS);
        Object result = JsonParser.findValueByKey(json, "implicit");
        return result instanceof Integer ? Duration.ofMillis((int) result) : Duration.ZERO;
    }

    protected static void setScriptWait(Duration duration) {
        String jsonToSend = new JsonBuilder().addKeyValue("script", duration.toMillis()).build();
        doPostRequest(EndPoints.TIMEOUTS, jsonToSend);
    }

    protected static Duration getScriptWait() {
        Map<String, Object> json = doGetRequest(EndPoints.TIMEOUTS);
        Object result = JsonParser.findValueByKey(json, "script");
        return result instanceof Integer ? Duration.ofMillis((int) result) : Duration.ZERO;
    }

    protected static void setPageLoad(Duration duration) {
        String jsonToSend = new JsonBuilder().addKeyValue("pageLoad", duration.toMillis()).build();
        doPostRequest(EndPoints.TIMEOUTS, jsonToSend);
    }

    protected static Duration getPageLoad() {
        Map<String, Object> json = doGetRequest(EndPoints.TIMEOUTS);
        Object result = JsonParser.findValueByKey(json, "pageLoad");
        return result instanceof Integer ? Duration.ofMillis((int) result) : Duration.ZERO;
    }


    protected static void back() {
        doPostRequest(EndPoints.BACK, "{}");
    }

    protected static void forward() {
        doPostRequest(EndPoints.FORWARD, "{}");
    }

    protected static void refresh() {
        doPostRequest(EndPoints.REFRESH, "{}");
    }

    protected static String title() {
        Map<String, Object> json = doGetRequest(EndPoints.GET_TITLE);
        return (String) JsonParser.findValueByKey(json, "value");
    }


    protected static class Contexts {

        protected static String getWindowHandle() {
            Map<String, Object> json = doGetRequest(EndPoints.GET_WINDOW_HANDLE);
            Object valueContent = JsonParser.findValueByKey(json, "value");
            if (valueContent instanceof String) {
                return (String) valueContent;
            }
            if (valueContent instanceof Map<?,?>) {
                return ((Map<?, ?>) valueContent).values().iterator().next().toString();
            }
            return null;
        }

        protected static void closeWindow() {

            boolean lastWindow = getWindowHandles().size() == 1;
            doDeleteRequest(EndPoints.GET_WINDOW_HANDLE);
            if (lastWindow) {
                SESSION_IDS.remove();
            } else {
                Set<String> handles = getWindowHandles();
                if (!handles.isEmpty()) {
                    switchToWindow(handles.iterator().next());
                }
            }

        }

        /**
         * @param handle The window handle we wish to switch to.
         * @throws NoSuchWindowException if no window is found with the specified handle.
         */
        protected static void switchToWindow(String handle) {
            Response response = doPostRequest(EndPoints.GET_WINDOW_HANDLE,  new JsonBuilder().addKeyValue("handle", handle).build());
            String m  = (String) JsonParser.findValueByKey(response, "message");
            HandleExceptions.handleResponse(response, "Window with handle: " + handle + " wasn't found!");
        }

        protected static Set<String> getWindowHandles() {
            Map<String, Object> json = doGetRequest(EndPoints.GET_WINDOW_HANDLES);
            Object handles = Objects.requireNonNull(JsonParser.findValueByKey(json, "value"));
            if (!(handles instanceof ArrayList<?>)) {
                return new HashSet<>();
            }
            return new HashSet<>((List<String>) handles);
        }

        protected static void newWindow() {
            String handle = doPostRequest(EndPoints.NEW_WINDOW, new JsonBuilder().addKeyValue("type", "window").build()).getValueAsMap().get("handle");
            switchToWindow(handle);
        }

        /**
         * Attempts to open a new Tab.
         * Since we can't open tabs if we don't create a new window we can use JS scripts to accomplish this;
         * Will use JS if normal window can't work.
         */
        protected static void newTab() {
            Set<String> beforeHandles = getWindowHandles();
            if (beforeHandles.size() != 1) {
                doPostRequest(EndPoints.NEW_WINDOW, new JsonBuilder().addKeyValue("type", "tab").build());
                return;
            }
            JsonBuilder json = new JsonBuilder().addKeyValue("script", "window.open('', '_blank');");
            json.addArray("args").build();
            String s = json.build();
            doPostRequest("/execute/sync", s);
            Set<String> afterHandles = getWindowHandles();
            for (String handle : beforeHandles) {
                afterHandles.remove(handle);
            }
            switchToWindow(afterHandles.iterator().next());
        }


        protected static void switchToDefaultContent() {
            doPostRequest(EndPoints.SWITCH_TO_FRAME, new JsonBuilder().addKeyValue("id", null).build());
        }

        protected static void switchToFrame(WebElement element) {
            Locator locator = element.getLocator();
            try {
                SearchContext searchContext = DriverClient.getElement(locator);
//                String key = searchContext.elementId.keySet().iterator().next();
                String json = String.format("{\"id\": {\"%s\": \"%s\"}}", searchContext.elementName(), searchContext.elementId());
                Response response = doPostRequest(EndPoints.SWITCH_TO_FRAME, json);
                response.getString("value");
            } catch (NoSuchElementException e) {
                System.out.println("Throwing NoSuchFrameException");
                throw new NoSuchFrameException("Frame " + locator.toString() + " was not found!");
            }
        }

        /**
         * Switch to Frame based on index.
         * @param index - 0 based.
         * @throws NoSuchFrameException will be thrown when frame with index can't be found.
         */
        protected static void switchToFrame(Number index) {
            Response response = doPostRequest(EndPoints.SWITCH_TO_FRAME, new JsonBuilder().addKeyValue("id", index).build());
            HandleExceptions.handleResponse(response, "Frame with index: " + index + " was not found!");
        }

    }
}
