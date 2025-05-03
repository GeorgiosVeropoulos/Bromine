package org.bromine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to be used on methods that can potentially throw exceptions.
 * Example a list.get(index) method can throw IndexOutOfBoundsException.
 * This is to be used when we want to mark a method that can throw an exception but without
 * adding throws clause to the method signature.
 *
 * <p>When applied to a method this indicates that a method can throw the mentioned exceptions</p>
 * <p>When applied to a class this indicates that all methods in the class can potentially throw the mentioned exceptions</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CanThrow {

    Class<? extends Throwable>[] value();

}
