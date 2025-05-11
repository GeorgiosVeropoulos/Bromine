package platform;

public class Platform {


    public enum OperatingSystem {
        WINDOWS,
        MAC,
        LINUX,
        NOT_SUPPORTED
    }

    public enum Architecture {
        X86,
        X64,
        ARM64,
        UNKNOWN
    }
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase();
    private static final OperatingSystem OS = detectOS();
    private static final Architecture ARCH = detectArch();

    // --- OS & Architecture Detection ---
    private static OperatingSystem detectOS() {
        if (OS_NAME.contains("win")) return OperatingSystem.WINDOWS;
        if (OS_NAME.contains("mac")) return OperatingSystem.MAC;
        if (OS_NAME.contains("nux") || OS_NAME.contains("nix")) return OperatingSystem.LINUX;
        return OperatingSystem.NOT_SUPPORTED;
    }

    private static Architecture detectArch() {
        if (OS_ARCH.contains("arm") || OS_ARCH.equals("aarch64")) {
            return Architecture.ARM64;
        }
        if (OS_ARCH.equals("x86") || OS_ARCH.equals("i386") || OS_ARCH.equals("i686")) {
            return Architecture.X86;
        }
        if (OS_ARCH.contains("64") || OS_ARCH.equals("amd64") || OS_ARCH.equals("x86_64")) {
            return Architecture.X64;
        }
        return Architecture.UNKNOWN;
    }

    public static OperatingSystem getOperatingSystem() {
        return OS;
    }

    public static Architecture getArchitecture() {
        return ARCH;
    }


    public static boolean isWindows() {
        return OS == OperatingSystem.WINDOWS;
    }

    public static boolean isMac() {
        return OS == OperatingSystem.MAC;
    }

    public static boolean isLinux() {
        return OS == OperatingSystem.LINUX;
    }
}
