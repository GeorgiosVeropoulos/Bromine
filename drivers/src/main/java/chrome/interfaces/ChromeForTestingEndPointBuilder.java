package chrome.interfaces;

import chrome.enums.SupportedBinaries;

import java.nio.file.Path;

public sealed interface ChromeForTestingEndPointBuilder<T extends ChromeForTestingEndPointBuilder<T>> permits LastKnownGoodBuilder, LatestVersionsPerMilestoneBuilder, LatestPathVersionsPerBuildBuilder {

    /**
     * The binary to use for the download.
     * <p>!This option must be set!</p>
     */
    T withBinary(SupportedBinaries binary);

    /**
     * The path to download the binary to.
     * <p>Users can choose to modify the downloadTo path with their own</p>
     * <p>Default is "target/downloads/{changed based on the builder implementing this}"</p>
     * @see chrome.Downloader for more info regarding the implementations.
     */
    T downloadTo(Path path);

    /**
     * The final command to execute the download.
     * @return the path where the binary was downloaded to.
     */
    Path execute();
}