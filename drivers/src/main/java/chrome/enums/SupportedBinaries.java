package chrome.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SupportedBinaries {
    CHROME("chrome"),
    CHROMEDRIVER("chromedriver"),
    CHROME_HEADLESS_SHELL("chrome-headless-shell");
    private final String binary;
}