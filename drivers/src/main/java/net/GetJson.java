package net;


import chrome.enums.SupportedEndPoints;
import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import chrome.jsons.LatestPatchVersionsPerBuildWithDownloads;
import chrome.jsons.LatestVersionsPerMilestoneWithDownloads;
import json.JsonMapper;
import json.JsonParser;

public class GetJson extends HttpHelper {

    public static LastKnownGoodVersionsWithDownloads getLastKnownGoodVersionsWithDownloadJson() {
        String json = get(SupportedEndPoints.LastKnownGoodVersionsWithDownloads.getUrl());
        return JsonMapper.of(LastKnownGoodVersionsWithDownloads.class).fromMap(JsonParser.parse(json));
    }

    public static LatestVersionsPerMilestoneWithDownloads getLatestVersionsPerMilestoneWithDownloadsJson() {
        String json = get(SupportedEndPoints.LatestVersionsPerMilestoneWithDownloads.getUrl());
        return JsonMapper.of(LatestVersionsPerMilestoneWithDownloads.class).fromMap(JsonParser.parse(json));
    }

    public static LatestPatchVersionsPerBuildWithDownloads getLatestPathVersionsPerBuildWithDownloadsJson() {
        String json = get(SupportedEndPoints.LatestPatchVersionsPerBuildWithDownloads.getUrl());
        return JsonMapper.of(LatestPatchVersionsPerBuildWithDownloads.class).fromMap(JsonParser.parse(json));
    }
}
