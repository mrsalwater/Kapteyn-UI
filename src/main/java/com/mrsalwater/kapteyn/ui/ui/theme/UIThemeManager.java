package com.mrsalwater.kapteyn.ui.ui.theme;

import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class UIThemeManager {

    private static final List<UIThemeChangeListener> LISTENERS = new ArrayList<>();

    private UIThemeManager() {

    }

    public static void setTheme(UITheme theme) {
        try {
            LookAndFeel lookAndFeel = theme.getClazz().getDeclaredConstructor().newInstance();
            UIManager.setLookAndFeel(lookAndFeel);
            FlatLaf.updateUI();

            for (UIThemeChangeListener listener : LISTENERS) {
                listener.onChange(theme);
            }
        } catch (UnsupportedLookAndFeelException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static UITheme getTheme() {
        return UITheme.match(UIManager.getLookAndFeel().getName());
    }

    public static void addListener(UIThemeChangeListener listener) {
        LISTENERS.add(listener);
    }

}
