package firefox;

public class GeckoVersion {

    String major;
    String minor;
    String patch;

    String completeString;

    public GeckoVersion(String completeString) {
        this.completeString = completeString;
        String[] parts = completeString.split("\\.");
        if (parts.length >= 3) {
            major = parts[0];
            minor = parts[1];
            patch = parts[2];
        } else {
            throw new IllegalArgumentException("Incomplete version: " + completeString);
        }
    }

    @Override
    public String toString() {
        return completeString;
    }
}
