package elements.actions;

import elements.WebElement;

public class ActionTemplate {

    public String keyA() {
        return "KeyA";
    }


    public String jsonToSend(WebElement element) {
        String elementValue = element.getSearchContext().elementName();
        String elementId = element.getSearchContext().elementId();
        return "{\"actions\":[{\"type\":\"pointer\",\"id\":\"mouse1\",\"parameters\":{\"pointerType\":\"mouse\"},\"actions\":[{\"type\":\"pointerMove\",\"origin\":{\"" + elementValue + "\":\"" + elementId + "\"},\"x\":0,\"y\":0,\"duration\":100},{\"type\":\"pointerDown\",\"button\":0},{\"type\":\"pointerUp\",\"button\":0}]}]}";
    }
}
