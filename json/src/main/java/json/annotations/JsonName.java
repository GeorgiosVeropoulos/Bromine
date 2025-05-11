package json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to specify the JSON field name for a class field.
 * This is useful when the JSON field name can't be directly mapped to the Java field name.
 * example : chrome-headless-shell
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JsonName {
    String value();
}
