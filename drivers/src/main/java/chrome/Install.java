package chrome;

import org.bromine.utils.files.Extract;

import java.nio.file.Path;

public class Install {



    public static void installChromeDriver(Path downloadedTo) {

        Extract.unzip(downloadedTo, downloadedTo.getParent());
    }
}
