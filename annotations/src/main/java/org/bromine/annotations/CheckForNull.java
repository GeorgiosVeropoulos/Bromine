package org.bromine.annotations;


import java.lang.annotation.*;

/**
 * Indicates that the annotated method may return null.
 * This annotation is used to indicate that the return value of a method
 * may be null, and should be checked for nullability before use.
 */
@Documented
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckForNull {

    String value() default "";

}
