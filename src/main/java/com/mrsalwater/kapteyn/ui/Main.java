package com.mrsalwater.kapteyn.ui;

import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.data.Config;
import com.mrsalwater.kapteyn.ui.ui.MainFrame;
import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;
import com.mrsalwater.kapteyn.ui.util.OperatingSystem;
import com.mrsalwater.kapteyn.ui.util.UIThemeUtil;

import javax.swing.*;

public final class Main {

    public static final String NAME = "Kapteyn";
    public static final String AUTHOR = "mrsalwater";
    public static final String VERSION = "v1.0";
    public static final String WEBSITE = "https://www.github.com/mrsalwater/Kapteyn-UI";

    private Main() {

    }

    public static void main(String[] args) {
        boolean initialized = AppData.initialize();
        if (initialized) {
            AppData.getLog().info("Initialized app data (path: \"" + AppData.getDirectoryFile().getPath() + "\")");
        }

        Config config = AppData.getConfig();
        AppData.getLog().info("Application running on " + OperatingSystem.getOperatingSystem().getName());

        UITheme theme;
        if (config.getUseSystemTheme()) {
            theme = UIThemeUtil.getSystemTheme();
            config.setTheme(theme);
            AppData.getLog().info("Using system ui theme \"" + theme.getDisplay() + "\"");
        } else {
            theme = config.getTheme();
            AppData.getLog().info("Using ui theme \"" + theme.getDisplay() + "\"");
        }

        UIThemeManager.setTheme(theme);

        SwingUtilities.invokeLater(MainFrame::new);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> AppData.getLog().close()));
    }

}
