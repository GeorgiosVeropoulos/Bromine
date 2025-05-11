package chrome.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SupportedChannels {
    STABLE("STABLE"),
    BETA("BETA"),
    DEV("DEV"),
    CANARY("CANARY");
    private final String channel;
}
