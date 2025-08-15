package chrome.interfaces;


import chrome.enums.SupportedBinaries;

import java.nio.file.Path;

public non-sealed interface LatestVersionsPerMilestoneBuilder extends ChromeForTestingEndPointBuilder<LatestVersionsPerMilestoneBuilder> {

    /**
     * The specific milestone we want to get.
     * @param milestone example: 134
     */
    LatestVersionsPerMilestoneBuilder withMilestone(String milestone);

}
