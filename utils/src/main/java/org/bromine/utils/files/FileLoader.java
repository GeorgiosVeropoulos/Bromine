package org.bromine.utils.files;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class FileLoader {



    public static URL getURLunderTargetClasses(String... path) {
        File file = new File(Paths.get("target/classes", path).toUri());
        URL url;

        if (!file.exists()) {
            return null;
        }

        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            url = null;
        }
        return url;
    }

    public static String getPathUnderTargetClasses(String... path) {
        try {
            // Build the full path to the file
            File file = Paths.get("target/classes", path).toFile();
            if (!file.exists()) {
                return null;
            }

            // Convert to canonical absolute path (platform safe, handles spaces, symlinks, etc.)
            return file.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
