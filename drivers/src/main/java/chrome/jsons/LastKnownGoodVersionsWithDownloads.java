package chrome.jsons;

import chrome.jsons.channels.Channels;
import lombok.Getter;

@Getter
public class LastKnownGoodVersionsWithDownloads {

    private String timestamp;
    private Channels channels;

    public LastKnownGoodVersionsWithDownloads() {
        // Needed for reflection
    }

    public LastKnownGoodVersionsWithDownloads(String timestamp, Channels channels) {
        this.timestamp = timestamp;
        this.channels = channels;
    }

}
