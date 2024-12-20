package client;

import elements.Locator;
import elements.WebElement;
import elements.WebDriver;
import json.JsonArrayBuilder;
import json.JsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sleeper.Sleeper;
import testbase.TestBase;

import static Constants.Constants.TWO_SECONDS;
import static elements.WebElementsFactory.$;

public class SendKeysTest extends TestBase {


    WebElement username = $(Locator.id("user-name"));


    @Test
    public void sendKeysTest() {
        WebDriver.get().open("https://www.saucedemo.com/v1/");

        username.sendKeys("Hello World");
        Sleeper.sleep(TWO_SECONDS);

        Assertions.assertEquals("Hello World", username.getAttribute("value"));

        JsonBuilder jsonBuilder = new JsonBuilder();

        JsonArrayBuilder jsonArrayBuilder = jsonBuilder.addArray("actions").addNestedObject()
                .addKeyValue("type", "key")
                .addKeyValue("id", "keyboard1")
                .addArray("actions");
        jsonArrayBuilder.addNestedObject()
                .addKeyValue("type", "keyDown").addKeyValue("value", "a");
        jsonArrayBuilder.addNestedObject()
                .addKeyValue("type", "keyUp").addKeyValue("value", "a");

//        String json = jsonArrayBuilder.build();
        String json = jsonBuilder.build();
//        username.clear();
//        Sleeper.sleep(TWO_SECONDS);
//        Assertions.assertEquals("", username.getAttribute("value"));
        String s = "{\n" +
                "      \"type\": \"pointer\",\n" +
                "      \"id\": \"mouse1\",\n" +
                "      \"parameters\": {\n" +
                "        \"pointerType\": \"mouse\"\n" +
                "      },\n" +
                "      \"actions\": [\n" +
                "        { \"type\": \"pointerMove\", \"duration\": 0, \"x\": 0, \"y\": 0, \"origin\": \"viewport\" },\n" +
                "        { \"type\": \"pointerDown\", \"button\": 0 },\n" +
                "        { \"type\": \"pointerMove\", \"duration\": 100, \"x\": 100, \"y\": 100, \"origin\": \"pointer\" },\n" +
                "        { \"type\": \"pointerUp\", \"button\": 0 }\n" +
                "      ]\n" +
                "    },";


    }
}
