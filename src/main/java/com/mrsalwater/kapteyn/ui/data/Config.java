package com.mrsalwater.kapteyn.ui.data;

import com.mrsalwater.kapteyn.decompiler.bytecode.settings.ByteCodeSetting;
import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.util.UIThemeUtil;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public final class Config {

    public static Config getDefaultConfig() {
        Map<String, Boolean> byteCodeSettings = new HashMap<>();
        for (ByteCodeSetting byteCodeSetting : ByteCodeSetting.values()) {
            byteCodeSettings.put(byteCodeSetting.getID(), byteCodeSetting.isDefaultValue());
        }

        return new Config(UIThemeUtil.getSystemTheme(), true, byteCodeSettings);
    }

    private UITheme theme;
    private boolean useSystemTheme;
    private Map<String, Boolean> byteCodeSettings;

    public Config(UITheme theme, boolean useSystemTheme, Map<String, Boolean> byteCodeSettings) {
        this.theme = theme;
        this.useSystemTheme = useSystemTheme;
        this.byteCodeSettings = byteCodeSettings;
    }

    public UITheme getTheme() {
        return theme;
    }

    public void setTheme(UITheme theme) {
        this.theme = theme;
        saveConfig();
    }

    public boolean getUseSystemTheme() {
        return useSystemTheme;
    }

    public void setUseSystemTheme(boolean useSystemTheme) {
        this.useSystemTheme = useSystemTheme;
        saveConfig();
    }

    public Map<String, Boolean> getByteCodeSettings() {
        return byteCodeSettings;
    }

    public void saveConfig() {
        try {
            AppData.saveConfigJSON(toString());
        } catch (FileNotFoundException e) {
            System.out.println("Cannot save config:");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();

        JSONObject objectPreferences = new JSONObject();
        objectPreferences.put("theme", theme.getDisplay());
        objectPreferences.put("useSystemTheme", useSystemTheme);

        JSONObject objectDecompiler = new JSONObject();
        for (String key : byteCodeSettings.keySet()) {
            objectDecompiler.put(key, byteCodeSettings.get(key));
        }

        json.put("preferences", objectPreferences);
        json.put("decompiler", objectDecompiler);

        return json.toString(4);
    }

}
