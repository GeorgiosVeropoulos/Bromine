package chrome.jsons;

import chrome.jsons.builds.Build;
import chrome.jsons.builds.Builds;
import chrome.jsons.milestones.Milestone;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
public class LatestPatchVersionsPerBuildWithDownloads {

    private String timestamp;
    private Builds builds;

    public LatestPatchVersionsPerBuildWithDownloads() {}

}
