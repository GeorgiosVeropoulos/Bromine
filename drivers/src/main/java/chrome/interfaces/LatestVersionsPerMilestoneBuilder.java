package chrome.interfaces;


import chrome.enums.SupportedBinaries;

import java.nio.file.Path;

public non-sealed interface LatestVersionsPerMilestoneBuilder extends EndPointBuilder {
    LatestVersionsPerMilestoneBuilder withBinary(SupportedBinaries binary);
    LatestVersionsPerMilestoneBuilder withMilestone(String milestone);
    LatestVersionsPerMilestoneBuilder downloadTo(Path path);
}
