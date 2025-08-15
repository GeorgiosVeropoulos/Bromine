package chrome.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All supported channels for Chrome for Testing.
 * Learn more info here: <a href="https://googlechromelabs.github.io/chrome-for-testing/">...</a>
 */
@AllArgsConstructor
@Getter
public enum SupportedChannels {
    STABLE("STABLE"),
    BETA("BETA"),
    DEV("DEV"),
    CANARY("CANARY");
    private final String channel;
}
