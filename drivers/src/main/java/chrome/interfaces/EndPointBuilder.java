package chrome.interfaces;

import java.nio.file.Path;

public sealed interface EndPointBuilder permits LastKnownGoodBuilder, LatestVersionsPerMilestoneBuilder, LatestPathVersionsPerBuildBuilder {
    Path execute();
}