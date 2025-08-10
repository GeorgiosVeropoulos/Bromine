package chrome.jsons;

import chrome.ChromeSupport;
import json.annotations.JsonName;
import lombok.Getter;

import java.util.List;

@Getter
public class Downloads {

    List<DownloadInfo> chrome;
    List<DownloadInfo> chromedriver;

    @JsonName("chrome-headless-shell")
    List<DownloadInfo> headlessShell;

    private DownloadInfo getInfoByPlatform(List<DownloadInfo> list, String platform) {
        if (list == null) {
            return null;
        }
        for (DownloadInfo info : list) {
            if (info.getPlatform().equalsIgnoreCase(platform)) {
                return info;
            }
        }
        return null;
    }


    /**
     * @return The download info for Chrome
     */
    public DownloadInfo getChromeInfoByPlatform() {
        return getInfoByPlatform(chrome, ChromeSupport.getCurrentPlatform().getPlatform());
    }

    /**
     * @return The download info for Chromedriver
     */
    public DownloadInfo getChromedriverInfoByPlatform() {
        return getInfoByPlatform(chromedriver, ChromeSupport.getCurrentPlatform().getPlatform());
    }

    /**
     * @return The download info for Chrome Headless Shell
     */
    public DownloadInfo getHeadlessShellInfoByPlatform() {
        return getInfoByPlatform(headlessShell, ChromeSupport.getCurrentPlatform().getPlatform());
    }

}
