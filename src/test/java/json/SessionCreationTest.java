package json;

import org.junit.jupiter.api.Test;

public class SessionCreationTest {




    @Test
    public void sessionCreationTest() {
        // Create the JSON builder instance
        JsonBuilder jsonBuilder = new JsonBuilder();

        // Build the JSON structure
        JsonObjectBuilder capabilities = jsonBuilder.addNestedObject("capabilities");
        JsonArrayBuilder firstMatchArray =
                capabilities.addArray("firstMatch");

        // Add a nested object to the array
//        JsonObjectBuilder firstMatchObject =
//                firstMatchArray
//                        .addNestedObject().addKeyValue("anotherKey", "anotherValue");
        firstMatchArray
                .addNestedObject().addKeyValue("browserName", "chrome").build();
//        firstMatchObject.addKeyValue("browserName", "chrome");

        // Get the final JSON string
        String jsonString = jsonBuilder.build();

        // Print the generated JSON
        System.out.println("Generated JSON:");
        System.out.println(jsonString);
    }
}
