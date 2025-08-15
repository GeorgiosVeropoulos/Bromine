package chrome.interfaces;

import chrome.enums.SupportedBinaries;

import java.nio.file.Path;

public non-sealed interface LatestPathVersionsPerBuildBuilder extends ChromeForTestingEndPointBuilder<LatestPathVersionsPerBuildBuilder> {


    /**
     * The specific build we want to download.
     * <p>It must be on this format {@code {major}.{minor}.{build}}</p>
     * @param build example: 115.0.5785
     */
    LatestPathVersionsPerBuildBuilder withBuild(String build);
}
