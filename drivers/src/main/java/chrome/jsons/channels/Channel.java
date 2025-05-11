package chrome.jsons.channels;

import chrome.ChromeSupport;
import chrome.Version;
import chrome.jsons.Downloads;
import lombok.Getter;


@Getter
public sealed class Channel permits Stable, Beta, Dev, Canary {

    protected String channel;

    protected String version;
    protected String revision;

    protected Downloads downloads;

    public Channel() {
    }

    public Version getVersion() {
        return new Version(version);
    }
}
