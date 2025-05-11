package chrome;

import chrome.enums.SupportedPlatforms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import platform.Platform;


/**
 * ChromeSupport is a utility class that provides methods to determine the current platform and its architecture.
 * It uses the Platform class to get the operating system and architecture information.
 */
public abstract class ChromeSupport {

    private ChromeSupport() {
        // Prevent instantiation
    }


    private static final Platform.Architecture architecture = Platform.getArchitecture();
    private static final Platform.OperatingSystem operatingSystem = Platform.getOperatingSystem();

    public static SupportedPlatforms getCurrentPlatform() {
        return switch (operatingSystem) {
            case WINDOWS -> switch (architecture) {
                case X86 -> SupportedPlatforms.WINDOWS_32;
                case X64 -> SupportedPlatforms.WINDOWS_64;
                default -> throw new UnsupportedOperationException("Unsupported architecture: " + architecture);
            };
            case MAC -> switch (architecture) {
                case ARM64 -> SupportedPlatforms.MAC_ARM;
                case X64 -> SupportedPlatforms.MAC_X;
                default -> throw new UnsupportedOperationException("Unsupported architecture: " + architecture);
            };
            case LINUX -> SupportedPlatforms.LINUX;
            default -> throw new UnsupportedOperationException("Unsupported operating system: " + operatingSystem);
        };
    }

}
