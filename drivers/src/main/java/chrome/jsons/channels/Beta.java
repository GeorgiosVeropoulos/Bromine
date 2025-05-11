package chrome.jsons.channels;

import chrome.jsons.Downloads;

public non-sealed class Beta extends Channel {
    public Beta() {
    }

    public Beta(String channel, String version, String revision, Downloads downloads) {
        this.channel = channel;
        this.version = version;
        this.revision = revision;
        this.downloads = downloads;
    }
}
