package com.mrsalwater.kapteyn.ui.ui;

import com.mrsalwater.kapteyn.decompiler.bytecode.settings.ByteCodeSetting;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.Main;
import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.data.Config;
import com.mrsalwater.kapteyn.ui.decompiler.Decompiler;
import com.mrsalwater.kapteyn.ui.ui.panel.help.AboutView;
import com.mrsalwater.kapteyn.ui.ui.panel.help.LegalView;
import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;
import com.mrsalwater.kapteyn.ui.util.UIThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public final class MainMenuBar extends JMenuBar {

    private final JMenuItem menuItemFileOpen;
    private final JMenuItem menuItemFileClose;
    private final JMenuItem menuItemFileSaveAs;
    private final JMenuItem menuItemFileSaveAll;
    private final JMenuItem menuItemFileFind;
    private final JMenuItem menuItemFileExit;

    private final JRadioButtonMenuItem menuItemSettingsThemeLight;
    private final JRadioButtonMenuItem menuItemSettingsThemeDark;
    private final JRadioButtonMenuItem menuItemSettingsUseSystemTheme;

    private final JMenuItem menuItemSettingsOpen;
    private final JMenuItem menuItemSettingsDelete;

    private final JMenuItem menuItemHelpWebsite;
    private final JMenuItem menuItemHelpLegal;
    private final JMenuItem menuItemHelpAbout;

    public MainMenuBar() {
        /* Menu File */

        JMenu menuFile = new JMenu("File");

        menuItemFileOpen = new JMenuItem("Open");
        menuItemFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        menuFile.add(menuItemFileOpen);

        menuItemFileClose = new JMenuItem("Close");
        menuFile.add(menuItemFileClose);

        menuFile.addSeparator();

        menuItemFileSaveAs = new JMenuItem("Save as");
        menuItemFileSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menuFile.add(menuItemFileSaveAs);

        menuItemFileSaveAll = new JMenuItem("Save all");
        menuItemFileSaveAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        menuFile.add(menuItemFileSaveAll);

        menuFile.addSeparator();

        menuItemFileFind = new JMenuItem("Find");
        menuItemFileFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        menuFile.add(menuItemFileFind);

        menuFile.addSeparator();

        menuItemFileExit = new JMenuItem("Exit");
        menuItemFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        menuFile.add(menuItemFileExit);

        add(menuFile);

        /* Menu Decompiler */

        JMenu menuDecompiler = new JMenu("Decompiler");

        for (ByteCodeSetting setting : ByteCodeSetting.values()) {
            JRadioButtonMenuItem menuItemDecompilerSetting = new JRadioButtonMenuItem(setting.getName(), setting.isDefaultValue());
            menuDecompiler.add(menuItemDecompilerSetting);

            Config config = AppData.getConfig();
            Map<String, Boolean> byteCodeSettings = config.getByteCodeSettings();
            menuItemDecompilerSetting.setSelected(byteCodeSettings.get(setting.getID()));

            menuItemDecompilerSetting.addActionListener(event -> {
                byteCodeSettings.put(setting.getID(), menuItemDecompilerSetting.isSelected());
                config.saveConfig();
            });
        }

        add(menuDecompiler);

        /* Menu Settings */

        JMenu menuSettings = new JMenu("Settings");

        // Menu Settings Theme

        JMenu menuItemTheme = new JMenu("Theme");

        menuItemSettingsThemeLight = new JRadioButtonMenuItem("Light");
        menuItemTheme.add(menuItemSettingsThemeLight);

        menuItemSettingsThemeDark = new JRadioButtonMenuItem("Dark");
        menuItemTheme.add(menuItemSettingsThemeDark);

        menuSettings.add(menuItemTheme);

        menuItemSettingsUseSystemTheme = new JRadioButtonMenuItem("Use system theme");
        menuSettings.add(menuItemSettingsUseSystemTheme);

        menuSettings.addSeparator();

        menuItemSettingsOpen = new JMenuItem("Open application files");
        menuSettings.add(menuItemSettingsOpen);

        menuItemSettingsDelete = new JMenuItem("Delete application files");
        menuSettings.add(menuItemSettingsDelete);

        add(menuSettings);

        // Menu Settings Init

        Config config = AppData.getConfig();

        UITheme theme = config.getTheme();
        if (theme == UITheme.LIGHT) {
            menuItemSettingsThemeLight.setSelected(true);
            menuItemSettingsThemeDark.setSelected(false);
        } else {
            menuItemSettingsThemeLight.setSelected(false);
            menuItemSettingsThemeDark.setSelected(true);
        }

        menuItemSettingsUseSystemTheme.setSelected(config.getUseSystemTheme());

        /* Menu Help */

        JMenu menuHelp = new JMenu("Help");

        menuItemHelpWebsite = new JMenuItem("Website");
        menuHelp.add(menuItemHelpWebsite);

        menuItemHelpLegal = new JMenuItem("Legal");
        menuHelp.add(menuItemHelpLegal);

        menuItemHelpAbout = new JMenuItem("About");
        menuHelp.add(menuItemHelpAbout);

        add(menuHelp);
    }

    public void initialize(Decompiler decompiler, MainFrame frame) {
        menuItemFileOpen.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(menuItemFileOpen);

            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                try {
                    decompiler.openFile(selectedFile);
                } catch (IOException e) {
                    AppData.getLog().error(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        menuItemFileClose.addActionListener(event -> decompiler.closeFile());

        menuItemFileSaveAs.addActionListener(event -> {
            if (decompiler.hasSelectedFile()) {
                String fileName = decompiler.getSelectedFile();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")).concat(".source");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(fileName));
                fileChooser.showSaveDialog(menuItemFileSaveAs);

                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    try {
                        decompiler.save(selectedFile);
                    } catch (FileNotFoundException e) {
                        AppData.getLog().error(e.getMessage());
                        JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });

        menuItemFileSaveAll.addActionListener(event -> {
            if (decompiler.hasFile()) {
                String fileName = decompiler.getFile();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")).concat(".zip");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(fileName));
                fileChooser.showSaveDialog(menuItemFileSaveAll);

                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    try {
                        decompiler.saveAll(selectedFile);
                    } catch (IOException | ClassFileException e) {
                        AppData.getLog().error(e.getMessage());
                        JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });

        menuItemFileExit.addActionListener(event -> System.exit(0));

        menuItemFileFind.addActionListener(event -> {
            if (decompiler.hasFile()) {
                frame.toggleFindMenu();
            }
        });

        menuItemSettingsThemeLight.addActionListener(event -> {
            if (menuItemSettingsThemeLight.isSelected()) {
                menuItemSettingsThemeDark.setSelected(false);
                setTheme(UITheme.LIGHT);
            } else {
                menuItemSettingsThemeLight.setSelected(true);
            }
        });

        menuItemSettingsThemeDark.addActionListener(event -> {
            if (menuItemSettingsThemeDark.isSelected()) {
                menuItemSettingsThemeLight.setSelected(false);
                setTheme(UITheme.DARK);
            } else {
                menuItemSettingsThemeDark.setSelected(true);
            }
        });

        menuItemSettingsUseSystemTheme.addActionListener(event -> {
            if (menuItemSettingsUseSystemTheme.isSelected()) {
                UITheme theme = UIThemeUtil.getSystemTheme();
                UIThemeManager.setTheme(theme);

                Config config = AppData.getConfig();
                config.setUseSystemTheme(true);
                config.setTheme(theme);

                if (theme == UITheme.LIGHT) {
                    menuItemSettingsThemeLight.setSelected(true);
                    menuItemSettingsThemeDark.setSelected(false);
                } else {
                    menuItemSettingsThemeLight.setSelected(false);
                    menuItemSettingsThemeDark.setSelected(true);
                }
            } else {
                Config config = AppData.getConfig();
                config.setUseSystemTheme(false);
            }
        });

        menuItemSettingsOpen.addActionListener(event -> {
            try {
                Desktop.getDesktop().open(AppData.getDirectoryFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuItemSettingsDelete.addActionListener(event -> {
            int dialogResult = JOptionPane.showConfirmDialog(frame, "Would you like to delete the app directory?", "Warning", JOptionPane.OK_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                boolean deleteResult = AppData.delete();

                if (!deleteResult) {
                    JOptionPane.showMessageDialog(frame, "Cannot delete app directory! Maybe another process is accessing it?", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        menuItemHelpWebsite.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(URI.create(Main.WEBSITE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuItemHelpLegal.addActionListener(event -> LegalView.display(frame));

        menuItemHelpAbout.addActionListener(event -> AboutView.display(frame));
    }

    private void setTheme(UITheme theme) {
        UIThemeManager.setTheme(theme);

        Config config = AppData.getConfig();
        config.setTheme(theme);

        if (config.getUseSystemTheme()) {
            config.setUseSystemTheme(false);
        }

        menuItemSettingsUseSystemTheme.setSelected(false);
    }

}
