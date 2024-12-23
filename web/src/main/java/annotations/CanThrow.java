package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to be used on methods that can potentially throw exceptions.
 * Example the method isDisplayed() can throw NoSuchElementException if the element is not available in the current DOM.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CanThrow {

    Class<? extends Throwable>[] value();

}
