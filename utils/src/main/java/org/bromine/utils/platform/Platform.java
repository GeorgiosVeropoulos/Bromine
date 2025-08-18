package org.bromine.utils.platform;

import com.sun.management.OperatingSystemMXBean;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

/**
 * A utility class to get information about the platform the code is running on.
 *
 * <p>Currently, this class provides methods to detect the operating system (OS)
 * and CPU architecture (Arch). Additional utility methods related to the platform
 * may be added in the future.</p>
 */
final public class Platform {

    private Platform() {
        //we don't want this class to be instanciated.
    }

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase();
    private static final OperatingSystem OS = detectOS();
    private static final Architecture ARCH = detectArch();

    @AllArgsConstructor
    @Getter
    public enum OperatingSystem implements PlatformIdentifier<OperatingSystem> {
        WINDOWS(new String[]{"win", "windows"}),
        MAC(new String[]{"mac", "darwin"}),
        LINUX(new String[]{"linux"}),
        NOT_SUPPORTED(new String[]{});

        private final String[] aliases;

        @Override
        public boolean matches(String text) {
            if (text == null) return false;
            text = text.toLowerCase();
            for (String alias : aliases) {
                if (text.contains(alias)) return true;
            }
            return false;
        }

        public static OperatingSystem current() {
            for (OperatingSystem os : values()) {
                if (os.matches(OS_NAME)) return os;
            }
            return NOT_SUPPORTED;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Architecture implements PlatformIdentifier<Architecture> {
            X86(new String[]{"x86", "win32", "linux32"}),
            X64(new String[]{"x64", "amd64", "win64", "linux64"}),
            ARM64(new String[]{"aarch64", "arm64", "win-arm64", "linux-arm64"}),
            UNKNOWN(new String[]{});

            private final String[] aliases;

        @Override
        public boolean matches(String text) {
            if (text == null) return false;
            text = text.toLowerCase();
            for (String alias : aliases) {
                if (text.contains(alias)) return true;
            }
            return false;
        }

        public static Architecture current() {
            for (Architecture a : values()) {
                if (a.matches(OS_ARCH)) return a;
            }
            return UNKNOWN;
        }
    }


    // --- OS & Architecture Detection ---
    private static OperatingSystem detectOS() {
        if (OS_NAME.contains("win")) return OperatingSystem.WINDOWS;
        if (OS_NAME.contains("mac")) return OperatingSystem.MAC;
        if (OS_NAME.contains("nux") || OS_NAME.contains("nix")) return OperatingSystem.LINUX;
        return OperatingSystem.NOT_SUPPORTED;
    }

    private static Architecture detectArch() {
        // Check ARM64 first
        if (OS_ARCH.equals("aarch64") || OS_ARCH.contains("arm")) {
            return Architecture.ARM64;
        }

        // Check 32-bit x86
        if (OS_ARCH.equals("x86") || OS_ARCH.equals("i386") || OS_ARCH.equals("i686")) {
            return Architecture.X86;
        }

        // Check 64-bit x86/AMD64
        if (OS_ARCH.equals("amd64") || OS_ARCH.equals("x86_64")) {
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

    //region  System info
    public static int getCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static String getGpuInfo() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (devices.length > 0) {
            return devices[0].getIDstring();
        }
        return "Unknown GPU";
    }
    //endregion

}
