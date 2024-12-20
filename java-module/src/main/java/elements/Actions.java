package elements;


import json.JsonArrayBuilder;
import json.JsonBuilder;
import json.JsonObjectBuilder;

/**
 * Class to access the Low level API of Actions as described in the WebDriver API.
 */
public class Actions {

    private JsonBuilder jsonBuilder = new JsonBuilder();


    /**
     * The Release Actions command is used to release all the keys and pointer buttons that are currently depressed.
     * This causes events to be fired as if the state was released by an explicit series of actions.
     * It also clears all the internal state of the virtual devices.
     */
    public void release() {
        HttpMethodExecutor.doDeleteRequest("/actions");
    }


    public void perform(String body) {

        //add here the bodyToSend
        HttpMethodExecutor.doPostRequest("/actions", body);
    }

    public void test() {

        JsonArrayBuilder actions = jsonBuilder.addArray("actionss");
//        actions.addNestedObject().addKeyValue("type")
    }

    public String createAction(WebElement element) {
        JsonArrayBuilder jsonArrayBuilder = jsonBuilder.addArray("actions");
        JsonObjectBuilder nestedObject = jsonArrayBuilder.addNestedObject();
        JsonObjectBuilder jsonObjectBuilder = nestedObject.addKeyValue("type", "pointer").addKeyValue("id", "mouse1").addNestedObject("parameters");
        jsonObjectBuilder.addKeyValue("pointerType", "mouse").build();
        JsonArrayBuilder jsonArrayBuilder1 = nestedObject.addArray("actions");
        jsonArrayBuilder1.addNestedObject().addKeyValue("type", "pointerMove").addNestedObject("origin")
                .addKeyValue(element.getSearchContext().elementName(), element.getSearchContext().elementId())
                .addKeyValue("x", 0)
                .addKeyValue("y", 0)
                .addKeyValue("duration", 100).build();
        jsonArrayBuilder1.addNestedObject().addKeyValue("type", "pointerDown").addKeyValue("button", 0);
        jsonArrayBuilder1.addNestedObject().addKeyValue("type", "pointerUp").addKeyValue("button", 0);
        String value = jsonBuilder.build();
        System.out.println(value);
        return value;
    }

}
