package com.mrsalwater.kapteyn.ui.util;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public final class UIThemeUtil {

    private UIThemeUtil() {

    }

    public static UITheme getSystemTheme() {
        switch (OperatingSystem.getOperatingSystem()) {
            case WINDOWS:
                return isWindowsThemeDarkMode() ? UITheme.DARK : UITheme.LIGHT;
            case MACOSX:
                return isMacMenuDarkMode() ? UITheme.DARK : UITheme.LIGHT;
            default:
                return UITheme.LIGHT;
        }
    }

    public static boolean isWindowsThemeDarkMode() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"REG", "QUERY", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "/v", "AppsUseLightTheme"});
            process.waitFor(100, TimeUnit.MILLISECONDS);

            if (process.exitValue() == 0) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.trim().startsWith("AppsUseLightTheme")) {
                        String value = line.split(" {4}")[3];
                        return value.equalsIgnoreCase("0x0");
                    }
                }

                bufferedReader.close();
            }
        } catch (IOException | InterruptedException | IllegalThreadStateException e) {
            System.out.println("Could not determine, whether 'dark mode' is being used. Falling back to default (light) mode.");
        }

        return false;
    }

    /* source: https://stackoverflow.com/a/33477375 */
    public static boolean isMacMenuDarkMode() {
        try {
            // check for exit status only. Once there are more modes than "dark" and "default", we might need to analyze string contents..
            Process process = Runtime.getRuntime().exec(new String[]{"defaults", "read", "-g", "AppleInterfaceStyle"});
            process.waitFor(100, TimeUnit.MILLISECONDS);
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException | IllegalThreadStateException ex) {
            // IllegalThreadStateException thrown by proc.exitValue(), if process didn't terminate
            System.out.println("Could not determine, whether 'dark mode' is being used. Falling back to default (light) mode.");
        }

        return false;
    }

}
