package chrome.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SupportedPlatforms {
    WINDOWS_32("win32", "chrome-win32.zip", "chromedriver-win32.zip", "chrome-headless-shell-win32.zip"),
    WINDOWS_64("win64", "chrome-win64.zip", "chromedriver-win64.zip", "chrome-headless-shell-win64.zip"),
    MAC_ARM("mac-arm64", "chrome-mac-arm64.zip", "chromedriver-mac-arm64.zip", "chrome-headless-shell-mac-arm64.zip"),
    MAC_X("mac_x64", "chrome-mac-x64.zip", "chromedriver-mac-x64.zip", "chrome-headless-shell-mac-x64.zip"),
    LINUX("linux64", "chrome-linux64.zip", "chromedriver-linux64.zip", "chrome-headless-shell-linux64.zip"),;
    private final String platform;
    private final String chromeZip;
    private final String chromedriverZip;
    private final String chromeHeadlessShellZip;
}