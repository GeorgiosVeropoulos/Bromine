package chrome;

import chrome.enums.SupportedChannels;
import chrome.enums.SupportedBinaries;
import chrome.jsons.DownloadInfo;
import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import chrome.jsons.channels.Channel;
import chrome.jsons.channels.Channels;
import net.GetJson;
import net.HttpHelper;
import org.bromine.annotations.UnderDevelopment;

import java.nio.file.Path;

import static chrome.ChromeSupport.getCurrentPlatform;

/**
 * Downloads the specified binary for the specified channel.
 * <p>
 * This class is used to download Chrome, Chromedriver, or Chrome Headless Shell binaries.
 * It uses the Last Known Good Versions with Downloads JSON file to get the download URL.
 * More endpoints will be added in the future.
 */
@UnderDevelopment
public class Downloader {

    private final SupportedBinaries binary;
    private final SupportedChannels channel;
    private final Path downloadTo;

    private static Path finalPath;

    Downloader(SupportedBinaries binary, SupportedChannels channel, Path downloadTo) {
        this.binary = binary;
        this.channel = channel;
        this.downloadTo = downloadTo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void execute() {
        LastKnownGoodVersionsWithDownloads data = GetJson.getLastKnownGoodVersionsWithDownloadJson();

        Channels channels = data.getChannels();
        Channel selectedChannel = switch (channel) {
            case STABLE -> channels.getStable();
            case BETA -> channels.getBeta();
            case DEV -> channels.getDev();
            case CANARY -> channels.getCanary();
        };

        String fileName;
        DownloadInfo info = switch (binary) {
            case CHROME -> {
                fileName = getCurrentPlatform().getChromeZip();
                yield selectedChannel.getDownloads().getChromeInfoByPlatform();
            }
            case CHROMEDRIVER -> {
                fileName = getCurrentPlatform().getChromedriverZip();
                yield selectedChannel.getDownloads().getChromedriverInfoByPlatform();
            }
            case CHROME_HEADLESS_SHELL -> {
                fileName = getCurrentPlatform().getChromeHeadlessShellZip();
                yield selectedChannel.getDownloads().getHeadlessShellInfoByPlatform();
            }
        };

        finalPath = downloadTo.resolve(fileName);
        HttpHelper.downloadTo(info.getUrl(), downloadTo.resolve(fileName));
    }

    public static class Builder {
        private SupportedBinaries binary;
        private SupportedChannels channel;
        private Path downloadTo;

        public Builder withBinary(SupportedBinaries binary) {
            this.binary = binary;
            return this;
        }

        public Builder withChannel(SupportedChannels channel) {
            this.channel = channel;
            return this;
        }

        public Builder downloadTo(Path path) {
            this.downloadTo = path;
            return this;
        }

        public Path execute() {
            if (binary == null || channel == null || downloadTo == null) {
                throw new IllegalStateException("Binary, channel, and download path must all be set.");
            }
            Downloader downloader = new Downloader(binary, channel, downloadTo);
            downloader.execute();  // Automatically runs
            return finalPath;
        }
    }
}

