package chrome.jsons;


import chrome.jsons.milestones.Milestone;
import json.annotations.JsonName;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class LatestVersionsPerMilestoneWithDownloads {

    private String timestamp;
    @JsonName("milestones")
    private LinkedHashMap<String, Milestone> milestones;

    public LatestVersionsPerMilestoneWithDownloads() {}

    public LatestVersionsPerMilestoneWithDownloads(String timestamp, LinkedHashMap<String, Milestone> milestones) {
        this.timestamp = timestamp;
        this.milestones = milestones;
    }

    public Milestone getMilestone(int index) {
        return milestones.get(index);
    }

    public Milestone getMilestone(String major) {
        Milestone milestone = milestones.get(major);
        if (milestone == null) {
            throw new NoSuchMilestonException(major);
        }
        return milestone;
    }


    private static class NoSuchMilestonException extends RuntimeException {
        public NoSuchMilestonException() {
            super();
        }

        public NoSuchMilestonException(String major) {
            super("Milestone with major release: " + major + " does not exist");
        }
    }
}
