package firefox;

import firefox.jsons.Release;
import json.JsonMapper;
import json.JsonParser;
import net.HttpHelper;

import java.util.List;

public class GeckoDownloader {


    public static List<Release> getReleases() {
        String json = HttpHelper.get("https://api.github.com/repos/mozilla/geckodriver/releases");
        return JsonMapper.of(Release.class).fromArray(JsonParser.parseArray(json));
    }
}
