package chrome.interfaces;


public interface EndpointSelector {

    LastKnownGoodBuilder lastKnownGoodVersions();
    LatestVersionsPerMilestoneBuilder latestVersionsPerMilestoneWithDownloads();

}