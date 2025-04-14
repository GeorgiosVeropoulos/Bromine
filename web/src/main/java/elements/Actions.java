package elements;


import json.JsonArrayBuilder;
import json.JsonBuilder;
import json.JsonObjectBuilder;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Class to access the Low level API of Actions as described in the WebDriver API.
 */
public class Actions {

    private static final ThreadLocal<JsonBuilder> actionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<SearchContext> elementSC = new ThreadLocal<>();

    JsonArrayBuilder actionsArray;

    public Actions(WebElement element) {
        SearchContext searchContext = element.getSearchContext();
        if (searchContext == null) {
            throw new IllegalArgumentException("The SearchContext of a WebElement should not be null!");
        }
        elementSC.set(searchContext);
        actionThreadLocal.set(new JsonBuilder());
        actionsArray = getJsonBuilder().addArray("actions");
    }

    private SearchContext getSearchContext() {
        return elementSC.get();
    }

    private JsonBuilder getJsonBuilder() {
        return actionThreadLocal.get();
    }

    /**
     * Move to a specific element.
     */
    public Actions moveToElement() {
        JsonObjectBuilder actionObject = actionsArray.addNestedObject();

        actionObject
                .addKeyValue("id", UUID.randomUUID().toString())
                .addKeyValue("type", "pointer")
                .addNestedObject("parameters").addKeyValue("pointerType", "mouse");

        JsonArrayBuilder pointerActions = actionObject.addArray("actions");
        SearchContext searchContext = getSearchContext();
        JsonObjectBuilder firstObject = pointerActions.addNestedObject()
                .addKeyValue("type", "pointerMove");
        firstObject
                .addNestedObject("origin")
                .addKeyValue(searchContext.elementName(), searchContext.elementId());
        firstObject
        // Reference to the element
                .addKeyValue("x", 0)  // Ensure x and y are integers
                .addKeyValue("y", 0)  // Ensure x and y are integers
                .addKeyValue("duration", 500);
        return this;
    }


    /**
     * Click an element.
     */
    public Actions click() {
//        moveToElement();
        JsonObjectBuilder pointerActionObject = actionsArray.addNestedObject();
        // Add the necessary "type" and "id" for the pointer action
        pointerActionObject
                .addKeyValue("type", "pointer")
                .addKeyValue("id", UUID.randomUUID().toString())
                .addNestedObject("parameters")
                .addKeyValue("pointerType", "mouse")
                .build();

        JsonArrayBuilder pointerActions = pointerActionObject.addArray("actions");

        // Correctly structure the pointerDown and pointerUp actions
        pointerActions.addNestedObject()
                .addKeyValue("type", "pointerDown")
                .addKeyValue("button", 0)
                .addKeyValue("duration", 100)
                .build();

        pointerActions.addNestedObject()
                .addKeyValue("type", "pointerUp")
                .addKeyValue("button", 0)
                .build();

        return this;
    }

    /**
     * Right-click (context click) an element.
     */
    public Actions contextClick() {
        moveToElement(); // This will handle pointerMove

        JsonObjectBuilder pointerActionObject = actionsArray.addNestedObject();
        // Add the pointer action structure (type, id, parameters)
        pointerActionObject
                .addKeyValue("type", "pointer")
                .addKeyValue("id", UUID.randomUUID().toString())
                .addNestedObject("parameters")
                .addKeyValue("pointerType", "mouse")
                .build();

        JsonArrayBuilder pointerActions = pointerActionObject.addArray("actions");

// Pointer Move (targeting an element)
        JsonObjectBuilder firstObject = pointerActions.addNestedObject().addKeyValue("type", "pointerMove");
        firstObject.addNestedObject("origin").addKeyValue(getSearchContext().elementName(), getSearchContext().elementId());
        firstObject
                .addKeyValue("x", 0)  // Optional offset
                .addKeyValue("y", 0)  // Optional offset
                .addKeyValue("duration", 500)
                .build();

// Right-click actions
        pointerActions.addNestedObject()
                .addKeyValue("type", "pointerDown")
                .addKeyValue("button", 2)
                .build();

        pointerActions.addNestedObject()
                .addKeyValue("type", "pointerUp")
                .addKeyValue("button", 2)
                .build();

// Final return
        return this;
    }

    /**
     * Builds the JSON, sends the request, and clears the ThreadLocal storage.
     */
    public void build() {
        String actionsJson = getJsonBuilder().build();

        // Debug: Print JSON before sending
        System.out.println("Executing Actions JSON: " + actionsJson);
        // Send the actions
        HttpMethodExecutor.doPostRequest(EndPoints.PERFORM_ACTIONS, actionsJson);
        HttpMethodExecutor.doDeleteRequest(EndPoints.RELEASE_ACTIONS);
        // Clear actions after execution
        actionThreadLocal.remove();
    }
}

