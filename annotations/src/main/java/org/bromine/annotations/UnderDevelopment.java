package org.bromine.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the code is under development.
 * It can be used to mark classes, methods, or fields that are not yet complete
 * and may change in the future.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnderDevelopment {
    /**
     * The reason why the code is under development.
     * @return the reason for the development.
     */
    String reason() default "Under development";
}
