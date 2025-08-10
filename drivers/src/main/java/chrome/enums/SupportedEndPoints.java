package chrome.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SupportedEndPoints {

    LastKnownGoodVersionsWithDownloads("https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json"),
    LatestVersionsPerMilestoneWithDownloads("https://googlechromelabs.github.io/chrome-for-testing/latest-versions-per-milestone-with-downloads.json"),
    LatestPatchVersionsPerBuildWithDownloads("https://googlechromelabs.github.io/chrome-for-testing/latest-patch-versions-per-build-with-downloads.json");

    private final String url;

}
