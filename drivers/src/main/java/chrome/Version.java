package chrome;


import lombok.Getter;


/**
 * Represents a version of Chrome in the format major.minor.build.patch
 */
@Getter
public class Version {

    private final String major;
    private final String minor;
    private final String build;
    private final String patch;

    private final String completeString;


    public Version(String completeString) {
        this.completeString = completeString;
        String[] parts = completeString.split("\\.");
        if (parts.length >= 4) {
            major = parts[0];
            minor = parts[1];
            build = parts[2];
            patch = parts[3];
        } else {
            throw new IllegalArgumentException("Incomplete version: " + completeString);
        }
    }

    public String toString() {
        return completeString;
    }
}
