package chrome.interfaces;

public non-sealed interface LatestPatchVersionsPerBuildBuilder extends ChromeForTestingEndPointBuilder<LatestPatchVersionsPerBuildBuilder> {


    /**
     * The specific build we want to download.
     * <p>It must be on this format {@code {major}.{minor}.{build}}</p>
     * @param build example: 115.0.5785
     */
    LatestPatchVersionsPerBuildBuilder withBuild(String build);
}
