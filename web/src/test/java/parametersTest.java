import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(parametersTest.Before.class)
public class parametersTest {

    // Source of parameters for the test
    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(1, 2, "1001", "Test case for doubling numbers 1"),
                Arguments.of(2, 4, "1002", "Test case for doubling numbers 2"),
                Arguments.of(3, 6, "1003", "Test case for doubling numbers 3"),
                Arguments.of(4, 8, "1004", "Test case for doubling numbers 4")
        );
    }

    // Parameterized test
    @ParameterizedTest
    @MethodSource("provideTestCases")
    @TestRail(caseId ="#{caseId}", name = "#{testName}")
    public void testDoubleValue(int input, int expectedOutput, String testRailId, String testName) {

        System.out.println("Running test with input: " + input + " and TestRail ID: " + testRailId + " and TestRail Name: " + testName);
        assertEquals(expectedOutput, doubleValue(input));
    }

    public int doubleValue(int num) {
        return num * 2;
    }


    public static class Before implements BeforeTestExecutionCallback{
        @Override
        public void beforeTestExecution(ExtensionContext context) throws Exception {
            // Get the test method
            Method testMethod = context.getRequiredTestMethod();

            // Check if the method is annotated with @TestRail
            if (testMethod.isAnnotationPresent(TestRail.class)) {
                // Get the @TestRail annotation instance
                TestRail testRailAnnotation = testMethod.getAnnotation(TestRail.class);

                // Extract the values passed to the @TestRail annotation
                String caseId = testRailAnnotation.caseId();
                String name = testRailAnnotation.name();

                // Print or log the values
                System.out.println("TestRail caseId: " + caseId);
                System.out.println("TestRail name: " + name);
            } else {
                System.out.println("No @TestRail annotation present on the method.");
            }
        }
    }



    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestRail {
        String caseId();
        String name();
    }
}
