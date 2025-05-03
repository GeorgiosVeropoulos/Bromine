package org.bromine.annotations;


import java.lang.annotation.*;


/**
 * Indicates that a class or method can be safely used by multiple threads concurrently.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadSafe {
}
