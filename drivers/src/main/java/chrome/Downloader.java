package chrome;

import chrome.enums.SupportedChannels;
import chrome.enums.SupportedBinaries;
import chrome.interfaces.EndpointSelector;
import chrome.interfaces.LastKnownGoodBuilder;
import chrome.interfaces.LatestPathVersionsPerBuildBuilder;
import chrome.interfaces.LatestVersionsPerMilestoneBuilder;
import chrome.jsons.DownloadInfo;
import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import chrome.jsons.LatestPatchVersionsPerBuildWithDownloads;
import chrome.jsons.LatestVersionsPerMilestoneWithDownloads;
import chrome.jsons.builds.Build;
import chrome.jsons.builds.Builds;
import chrome.jsons.channels.Channel;
import chrome.jsons.channels.Channels;
import chrome.jsons.milestones.Milestone;
import net.GetJson;
import net.HttpHelper;

import java.nio.file.Path;

import static chrome.ChromeSupport.getCurrentPlatform;

/**
 * Downloads the specified binary for the specified channel.
 * <p>
 * This class is used to download Chrome, Chromedriver, or Chrome Headless Shell binaries.
 * It uses the Last Known Good Versions with Downloads JSON file to get the download URL.
 * More endpoints will be added in the future.
 */
import java.nio.file.Paths;

public class Downloader {

    private static Path finalPath;


    public static EndpointSelector builderFor() {
        return new EndpointSelector() {
            @Override
            public LastKnownGoodBuilder lastKnownGoodVersions() {
                return new LastKnownGoodBuilderImpl();
            }

            @Override
            public LatestVersionsPerMilestoneBuilder latestVersionsPerMilestoneWithDownloads() {
                return new LatestVersionsPerMilestoneBuilderImpl();
            }

            @Override
            public LatestPathVersionsPerBuildBuilder latestPathVersionsPerBuildWithDownloads() {
                return new LatestPathVersionsPerBuildBuilderImpl();
            }
        };
    }
    private static class LastKnownGoodBuilderImpl implements LastKnownGoodBuilder {

        private LastKnownGoodBuilderImpl(){

        }
        // implement methods returning this
        private SupportedBinaries binary;
        private SupportedChannels channel = SupportedChannels.STABLE;
        private Path downloadTo = Paths.get("target", "downloads", channel.getChannel().toLowerCase());

        public LastKnownGoodBuilder withBinary(SupportedBinaries binary) {
            this.binary = binary;
            return this;
        }

        public LastKnownGoodBuilder withChannel(SupportedChannels channel) {
            this.channel = channel;
            return this;
        }

        public LastKnownGoodBuilder downloadTo(Path path) {
            this.downloadTo = path;
            return this;
        }

        public Path execute() {
            if (binary == null) {
                throw new IllegalArgumentException("Binary must be set in order to download");
            }
            LastKnownGoodVersionsWithDownloads data = GetJson.getLastKnownGoodVersionsWithDownloadJson();

            Channels channels = data.getChannels();
            Channel selectedChannel = switch (channel) {
                case STABLE -> channels.getStable();
                case BETA -> channels.getBeta();
                case DEV -> channels.getDev();
                case CANARY -> channels.getCanary();
            };
            DownloadInfo info = selectedChannel.getDownloads().getInfoByBinary(binary);
            String fileName = getCurrentPlatform().getZipByBinary(binary);
            finalPath = downloadTo.resolve(fileName);
            HttpHelper.downloadTo(info.getUrl(), downloadTo.resolve(fileName));
            return finalPath;
        }
    }

    private static class LatestVersionsPerMilestoneBuilderImpl implements LatestVersionsPerMilestoneBuilder {
        // implement methods returning this
        private SupportedBinaries binary;
        private String milestone;
        private Path downloadTo = Paths.get("target", "downloads", "milestone");
        private LatestVersionsPerMilestoneBuilderImpl(){

        }

        public LatestVersionsPerMilestoneBuilder withBinary(SupportedBinaries binary) {
            this.binary = binary;
            return this;
        }

        @Override
        public LatestVersionsPerMilestoneBuilder withMilestone(String milestone) {
            this.milestone = milestone;
            return this;
        }

        @Override
        public LatestVersionsPerMilestoneBuilder downloadTo(Path path) {
            this.downloadTo = path;
            return this;
        }


        @Override
        public Path execute() {
            if (binary == null) {
                throw new IllegalArgumentException("Binary must be set in order to download");
            }
            LatestVersionsPerMilestoneWithDownloads data = GetJson.getLatestVersionsPerMilestoneWithDownloadsJson();

            Milestone m = data.getMilestone(milestone);

            String fileName;
            DownloadInfo info = m.getDownloads().getInfoByBinary(binary);
            fileName = getCurrentPlatform().getZipByBinary(binary);
            downloadTo = Paths.get(downloadTo.toString(), milestone);
            finalPath = downloadTo.resolve(fileName);
            if (info == null) {
                throw new IllegalArgumentException("There is no info for " + binary.getBinary() + " for milestone: " + milestone);
            }
            HttpHelper.downloadTo(info.getUrl(), downloadTo.resolve(fileName));
            return finalPath;
        }
    }

    private static class LatestPathVersionsPerBuildBuilderImpl implements LatestPathVersionsPerBuildBuilder {
        // implement methods returning this
        private SupportedBinaries binary;
        private String build;
        private Path downloadTo = Paths.get("target", "downloads", "build");
        private LatestPathVersionsPerBuildBuilderImpl(){

        }

        @Override
        public LatestPathVersionsPerBuildBuilder withBinary(SupportedBinaries binary) {
            this.binary = binary;
            return this;
        }

        @Override
        public LatestPathVersionsPerBuildBuilder withBuild(String build) {
            this.build = build;
            return this;
        }

        @Override
        public LatestPathVersionsPerBuildBuilder downloadTo(Path path) {
            this.downloadTo = path;
            return this;
        }


        @Override
        public Path execute() {
            if (binary == null) {
                throw new IllegalArgumentException("Binary must be set in order to download");
            }
            LatestPatchVersionsPerBuildWithDownloads data = GetJson.getLatestPathVersionsPerBuildWithDownloadsJson();

            Build build = data.getBuilds().getBuild(this.build);

            String fileName;
            DownloadInfo info = build.getDownloads().getInfoByBinary(binary);
            fileName = getCurrentPlatform().getZipByBinary(binary);
            downloadTo = Paths.get(downloadTo.toString(), this.build);
            finalPath = downloadTo.resolve(fileName);
            if (info == null) {
                throw new IllegalArgumentException("There is no info for " + binary.getBinary() + " for build: " + this.build);
            }
            HttpHelper.downloadTo(info.getUrl(), downloadTo.resolve(fileName));
            return finalPath;
        }
    }
}

