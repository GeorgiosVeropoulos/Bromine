package org.bromine.utils.platform;

public interface PlatformIdentifier<T extends Enum<T> & PlatformIdentifier<T>> {

    /**
     * Check if the given text contains any alias for this platform.
     */
    boolean matches(String text);

    /**
     * Get the current platform (OS or Arch) for the running system.
     */
    static <T extends Enum<T> & PlatformIdentifier<T>> T current() {
        throw new UnsupportedOperationException("Must be implemented in enum");
    }
}
