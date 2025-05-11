package net;


import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import json.JsonMapper;
import json.JsonParser;

public class GetJson extends HttpHelper {

    public static LastKnownGoodVersionsWithDownloads getLastKnownGoodVersionsWithDownloadJson() {
        String json = get("https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json");
        return JsonMapper.of(LastKnownGoodVersionsWithDownloads.class).fromMap(JsonParser.parse(json));

    }
}
