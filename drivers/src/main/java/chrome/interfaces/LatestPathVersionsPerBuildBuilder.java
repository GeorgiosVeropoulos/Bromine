package chrome.interfaces;

import chrome.enums.SupportedBinaries;

import java.nio.file.Path;

public non-sealed interface LatestPathVersionsPerBuildBuilder extends EndPointBuilder {

    LatestPathVersionsPerBuildBuilder withBinary(SupportedBinaries binary);
    LatestPathVersionsPerBuildBuilder withBuild(String build);
    LatestPathVersionsPerBuildBuilder downloadTo(Path path);
}
