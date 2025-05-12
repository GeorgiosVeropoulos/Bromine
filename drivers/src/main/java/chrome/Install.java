package chrome;

import org.bromine.utils.zip.ZipHelper;

import java.nio.file.Path;

public class Install {



    public static void installChromeDriver(Path downloadedTo) {

        ZipHelper.unzip(downloadedTo, downloadedTo.getParent());
    }
}
