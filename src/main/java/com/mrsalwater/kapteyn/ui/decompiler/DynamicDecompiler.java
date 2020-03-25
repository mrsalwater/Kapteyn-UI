package com.mrsalwater.kapteyn.ui.decompiler;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.ui.panel.code.CodePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.file.FilePanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public final class DynamicDecompiler extends Decompiler {

    private Decompiler decompiler = new EmptyDecompiler();

    public DynamicDecompiler(CodePanel codePanel, FilePanel filePanel) {
        super(codePanel, filePanel);
    }

    @Override
    public void openFile(File file) throws IOException {
        if (isValidFile(file)) {
            String fileExtension = getFileExtension(file);

            if (fileExtension.equals("class")) {
                decompiler = new ClassFileDecompiler(codePanel, filePanel);
            } else if (fileExtension.equals("jar")) {
                decompiler = new JarFileDecompiler(codePanel, filePanel);
            }

            decompiler.openFile(file);
            AppData.getLog().info("Opened file: " + file.getName());
        }
    }

    @Override
    public void closeFile() {
        decompiler.closeFile();
        AppData.getLog().info("Closed file");
    }

    @Override
    public void selectFile(String name) throws IOException, ClassFileException {
        decompiler.selectFile(name);
        AppData.getLog().info("Selected file: " + name);
    }

    @Override
    public void save(File destination) throws FileNotFoundException {
        decompiler.save(destination);
        AppData.getLog().info("Saved file: " + destination);
    }

    @Override
    public void saveAll(File destination) throws IOException, ClassFileException {
        decompiler.saveAll(destination);
        AppData.getLog().info("Saved all: " + destination);
    }

    @Override
    public boolean hasFile() {
        return decompiler.hasFile();
    }

    @Override
    public String getFile() {
        return decompiler.getFile();
    }

    @Override
    public boolean hasSelectedFile() {
        return decompiler.hasSelectedFile();
    }

    @Override
    public String getSelectedFile() {
        return decompiler.getSelectedFile();
    }

    @Override
    public String getSource(String name) throws IOException, ClassFileException {
        return decompiler.getSource(name);
    }

    @Override
    public List<ClassFile> getClassFiles() throws IOException, ClassFileException {
        return decompiler.getClassFiles();
    }

    private boolean isValidFile(File file) {
        if (file.getName().contains(".")) {
            String fileExtension = getFileExtension(file);
            return fileExtension.equals("class") || fileExtension.equals("jar");
        }

        return false;
    }

    private String getFileExtension(File file) {
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }

}
