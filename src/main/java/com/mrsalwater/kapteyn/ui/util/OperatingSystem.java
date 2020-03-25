package com.mrsalwater.kapteyn.ui.util;

public enum OperatingSystem {

    WINDOWS("Windows"),
    LINUX("Linux"),
    MACOSX("OS X"),
    UNKNOWN("Unknown");

    private final String name;

    OperatingSystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static OperatingSystem getOperatingSystem() {
        String osName = System.getProperty("os.name");

        if (osName != null) {
            for (OperatingSystem operatingSystem : OperatingSystem.values()) {
                if (osName.contains(operatingSystem.getName())) {
                    return operatingSystem;
                }
            }
        }

        return UNKNOWN;
    }

}
