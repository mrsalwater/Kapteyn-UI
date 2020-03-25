package com.mrsalwater.kapteyn.ui.data;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.util.OperatingSystem;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AppData {

    private static final String DIRECTORY_NAME = "Kapteyn";
    private static final String CONFIG_NAME = "config.json";
    private static final String LOG_NAME = "latest.log";

    private static Log LOG;
    private static Config CONFIG;

    public static boolean initialize() {
        LOG = Log.create();

        String appDataPath = getAppDataPath();
        if (appDataPath == null) {
            LOG.warning("Cannot get app data path!");
            return false;
        }

        File directory = new File(appDataPath.concat("\\").concat(DIRECTORY_NAME));
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (!result) {
                LOG.warning("Cannot create app data directory!");
                return false;
            }
        } else if (!directory.isDirectory()) {
            LOG.warning("App data directory path is already in use!");
            return false;
        }

        Log.initialize(LOG);

        File config = new File(directory.getPath().concat("\\").concat(CONFIG_NAME));
        if (!config.exists()) {
            boolean result;
            try {
                result = config.createNewFile();
                if (!result) {
                    LOG.warning("Cannot create app data config!");
                    throw new IOException();
                }
            } catch (IOException e) {
                LOG.warning("Cannot create app data config:");
                e.printStackTrace();
                return false;
            } finally {
                try {
                    CONFIG = Config.getDefaultConfig();
                    saveConfigJSON(CONFIG.toString());
                } catch (FileNotFoundException e) {
                    LOG.warning("Cannot create default app data config:");
                    e.printStackTrace();
                }
            }
        } else {
            try {
                String rawJSON = getConfigJSON();
                JSONObject json = new JSONObject(new JSONTokener(rawJSON));

                JSONObject objectPreferences = (JSONObject) json.get("preferences");
                UITheme theme = UITheme.matchDisplay((String) objectPreferences.get("theme"));
                boolean useSystemTheme = (boolean) objectPreferences.get("useSystemTheme");

                JSONObject objectDecompiler = (JSONObject) json.get("decompiler");
                Map<String, Boolean> byteCodeSettings = new HashMap<>();
                for (String key : objectDecompiler.keySet()) {
                    byteCodeSettings.put(key, objectDecompiler.getBoolean(key));
                }

                CONFIG = new Config(theme, useSystemTheme, byteCodeSettings);
            } catch (IOException e) {
                CONFIG = Config.getDefaultConfig();
                LOG.warning("Cannot read app data config:");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static boolean delete() {
        LOG.close();
        File directory = getDirectoryFile();

        if (directory.exists()) {
            boolean result = deleteDir(directory);

            if (!result) {
                LOG.warning("Cannot delete app directory");
                return false;
            } else {
                LOG.info("Deleted app directory");
            }
        } else {
            LOG.warning("Cannot delete app directory (not existing)");
        }

        return true;
    }

    public static Log getLog() {
        return LOG;
    }

    public static Config getConfig() {
        return CONFIG;
    }

    public static void saveConfigJSON(String json) throws FileNotFoundException {
        File configFile = getConfigFile();

        try (PrintWriter out = new PrintWriter(configFile)) {
            out.println(json);
        }
    }

    public static String getConfigJSON() throws IOException {
        File config = getConfigFile();
        return new String(Files.readAllBytes(config.toPath()));
    }

    public static File getLogFile() {
        return new File(getDirectoryFile().getPath().concat("\\").concat(LOG_NAME));
    }

    public static File getConfigFile() {
        return new File(getDirectoryFile().getPath().concat("\\").concat(CONFIG_NAME));
    }

    public static File getDirectoryFile() {
        return new File(Objects.requireNonNull(getAppDataPath()).concat("\\").concat(DIRECTORY_NAME));
    }

    private static String getAppDataPath() {
        switch (OperatingSystem.getOperatingSystem()) {
            case WINDOWS:
                return System.getenv("APPDATA");
            case LINUX:
                // TODO: 02.02.2020
            case MACOSX:
                // TODO: 02.02.2020 System.getProperty("user.home") + ~/Library/Preferences ?
            default:
                return null;
        }
    }

    private static boolean deleteDir(File file) {
        File[] contents = file.listFiles();

        if (contents != null) {
            for (File subFile : contents) {
                if (!Files.isSymbolicLink(subFile.toPath())) {
                    boolean result = deleteDir(subFile);

                    if (!result) {
                        return false;
                    }
                }
            }
        }

        return file.delete();
    }

}
