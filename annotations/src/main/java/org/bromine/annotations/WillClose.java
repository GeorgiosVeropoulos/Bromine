package org.bromine.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the annotated method will close a resource.
 * This annotation is used to indicate that the method will close a resource
 * such as a file, network connection, or database connection.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WillClose {

    Class<?> clazz() default Object.class;

    String info() default "";
}
