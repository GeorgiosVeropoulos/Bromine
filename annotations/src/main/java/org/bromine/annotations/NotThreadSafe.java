package org.bromine.annotations;


import java.lang.annotation.*;

/**
 * Indicates that a class or method is not thread-safe and should not be used by multiple threads concurrently.
 * This annotation can be used to mark classes or methods that are not designed to be thread-safe.
 * It serves as a warning to developers that the annotated element may not behave correctly when accessed
 * by multiple threads at the same time.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotThreadSafe {
}
