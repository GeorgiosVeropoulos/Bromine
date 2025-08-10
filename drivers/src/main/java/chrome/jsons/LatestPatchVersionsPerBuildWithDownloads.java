package chrome.jsons;

import chrome.jsons.builds.Builds;
import chrome.jsons.milestones.Milestone;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Getter
public class LatestPatchVersionsPerBuildWithDownloads {

    private String timestamp;
    private Builds builds;

    public LatestPatchVersionsPerBuildWithDownloads() {}

    public LatestPatchVersionsPerBuildWithDownloads(String timestamp, Builds builds) {
        this.timestamp = timestamp;
        this.builds = builds;
    }
}
