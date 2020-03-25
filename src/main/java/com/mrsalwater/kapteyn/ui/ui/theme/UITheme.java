package com.mrsalwater.kapteyn.ui.ui.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public enum UITheme {

    LIGHT(FlatLightLaf.class, "Flat Light", "Light"),
    DARK(FlatDarkLaf.class, "Flat Dark", "Dark");

    private final Class<? extends LookAndFeel> clazz;
    private final String name;
    private final String display;

    UITheme(Class<? extends LookAndFeel> clazz, String name, String display) {
        this.clazz = clazz;
        this.name = name;
        this.display = display;
    }

    public Class<? extends LookAndFeel> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public static UITheme match(String name) {
        for (UITheme uiTheme : values()) {
            if (uiTheme.getName().equals(name)) {
                return uiTheme;
            }
        }

        throw new NullPointerException();
    }

    public static UITheme matchDisplay(String display) {
        for (UITheme uiTheme : values()) {
            if (uiTheme.getDisplay().equals(display)) {
                return uiTheme;
            }
        }

        throw new NullPointerException();
    }

}
