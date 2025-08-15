package firefox.jsons;

import ch.qos.logback.classic.spi.PlatformInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.HttpHelper;
import org.bromine.utils.platform.Platform;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Release {

    String url;
    String assets_url;
    String upload_url;
    String html_url;
    Long id;
    Author author;
    String node_id;
    String tag_name;
    String target_commitish;
    String name;
    Boolean draft;
    Boolean immutable;
    Boolean prerelease;
    String created_at;
    String published_at;
    List<Asset> assets;
    String tarball_url;
    String zipball_url;
    String body;


    public Release() {

    }

    public Path getGeckoDriver() {
        Platform.Architecture arch = Platform.getArchitecture();
        String url = null;
        String fileName = "geckodriver";
        for (Asset asset : assets) {

            url = asset.getBrowser_download_url();
            if (url.contains(".asc")) {
                continue; // Skip signature files
            }

            // Match OS
            if (Platform.isLinux()) {
                if (arch == Platform.Architecture.ARM64 && url.contains("aarch64")) {
                    fileName = fileName.concat(".tar.gz");
                    break;
                } else if (arch == Platform.Architecture.X64 && url.contains("64") && !url.contains("aarch64")) {
                    fileName = fileName.concat(".tar.gz");
                    break;
                } else if (arch == Platform.Architecture.X86 && url.contains("32")) {
                    fileName = fileName.concat(".tar.gz");
                    break;
                }
            } else if (Platform.isWindows()) {
                if (arch == Platform.Architecture.X64 && url.contains("64")) {
                   break;
                } else if (arch == Platform.Architecture.X86 && url.contains("32")) {
                   break;
                }
            } else if (Platform.isMac()) {
               break;
            }
        }
        Path path = Paths.get("target", "downloads", "geckodriver", fileName);
        log.info("Downloading geckodriver from: {}", url);
        HttpHelper.downloadTo(url, path);
        return path;
    }
}
