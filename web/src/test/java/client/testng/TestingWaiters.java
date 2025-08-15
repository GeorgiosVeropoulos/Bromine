package client.testng;

import capabilities.Configuration;
import chrome.*;
import chrome.enums.SupportedBinaries;
import chrome.enums.SupportedChannels;
import chrome.jsons.DownloadInfo;
import chrome.jsons.Downloads;
import chrome.jsons.LastKnownGoodVersionsWithDownloads;
import chrome.jsons.channels.Channel;
import conditions.Be;
import conditions.Have;
import elements.*;
import firefox.GeckoDownloader;
import firefox.jsons.Release;
import lombok.extern.slf4j.Slf4j;
import net.GetJson;
import org.bromine.utils.platform.Platform;
import org.bromine.utils.files.Extract;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Page;
import sleeper.Sleep;
import testbase.TestBaseTestNG;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@Slf4j
public class TestingWaiters extends TestBaseTestNG {

    public Page page;

    @BeforeMethod(alwaysRun = true)
    public void before() {
        page = new Page();
    }


    @Test(groups = "waitToBe")
    public void test1() throws IOException {
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        Version details = Chrome.getChromeDetails();
        LastKnownGoodVersionsWithDownloads lastKnownGoodVersionsWithDownloads = GetJson.getLastKnownGoodVersionsWithDownloadJson();
        Downloads downloadInfos = lastKnownGoodVersionsWithDownloads.getChannels().getStable().getDownloads();
//        Channels channels = JsonMapper.of(Channels.class).fromMap(json);
//        Map<String, Object> stable = (Map<String, Object>) json.get("Stable");
        String platforms = downloadInfos.getChrome().get(0).getPlatform();
        DownloadInfo windows = downloadInfos.getChromeInfoByPlatform();
        Channel stable = lastKnownGoodVersionsWithDownloads.getChannels().getStable();
        Version detals1 = stable.getVersion();
        Path path = Downloader.builderFor().lastKnownGoodVersions()
                .withBinary(SupportedBinaries.CHROMEDRIVER)
                .withChannel(SupportedChannels.STABLE)
                .downloadTo(Path.of("target", "test"))
                .execute();
        WebDriver.get().open("https://www.georgeveropoulos.com");
//        Path path = Downloader.builder().withBinary(SupportedBinaries.CHROME)
//                .withChannel(SupportedChannels.STABLE)
//                .downloadTo(Path.of("target", "test"))
//                .execute();
        Install.installChromeDriver(path);
//        Files.deleteIfExists(Path.of("target", "test", "chromedriver-win64.zip"));
        log.info("Path: " + path);

        log.info("OS: " + os);
//        WebDriver.get().open("https://www.georgeveropoulos.com");
//        Configuration.waiters().setTimeout(Duration.ofSeconds(5));
//        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(5));
////        Assert.assertThrows(TimeOutException.class, () -> {
////            $(Locator.xpath("//a1231321")).waitToBe(Condition.visible);
////        });
//        $(Locator.xpath("//a1231321")).waitTo(Be.visible).click();
//        boolean exists = page.info.exists();
//        log.info("Element exists: " + exists);
    }

    @Test(groups = "waitToBe")
    public void test2() {
        WebDriver.get().open("https://www.georgeveropoulos.com");
        Configuration.waiters().setTimeout(Duration.ofSeconds(5));
        WebDriver.get().timeouts().set().implicitWait(Duration.ofSeconds(5));
//        $(Locator.xpath("//a1231321")).waitToBe(Condition.visible);
        page.info.waitTo(Be.visible).click();
        log.info("Text to be Info: " + page.info.waitTo(Have.text("Info")).getText());
        Sleep.ForSeconds(3);
        log.info(page.info.getSearchContext().toString());

//        Sleeper.sleep(Duration.ofSeconds(5));
//        elementList.get(0).click();
//        boolean exists = page.info.exists();
//        System.out.println("Element exists: " + exists);
//        boolean exists = $(Locator.xpath("//a1231321")).exists();
//        System.out.println("Element exists: " + exists);
    }

    @Test
    public void testEndPointsChromeDownloader() {

       Path path1 = Downloader.builderFor()
               .latestVersionsPerMilestoneWithDownloads()
               .withBinary(SupportedBinaries.CHROMEDRIVER)
//               .withMilestone("114")
               .withMilestone("134")
               .execute();

    }

    @Test
    public void getLatestPathVersionsPerBuildWithDownloadsJson() {

        Downloader.builderFor()
                .latestVersionsPerMilestoneWithDownloads()
                .execute();

        Path path1 = Downloader.builderFor()
                .latestPathVersionsPerBuildWithDownloads()
                .withBinary(SupportedBinaries.CHROMEDRIVER)
//               .withMilestone("114")
//                .withBuild("113.0.5672")
//                .withBuild("114.0.5696")
                .withBuild("115.0.5785")
                .execute();

    }

    @Test
    public void testGecko() {
        List<Release> releases = GeckoDownloader.getReleases();
        Release release = releases.get(0);
        Path path = release.getGeckoDriver();
        Extract.file(path, path.getParent());


        System.out.println("");
    }

    @Test
    public void geckoDriverTest() {
        Sleep.with(Duration.ofSeconds(10));
    }
}
