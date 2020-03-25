package com.mrsalwater.kapteyn.ui.decompiler;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.ui.panel.code.CodePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.file.FilePanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class Decompiler {

    protected final CodePanel codePanel;
    protected final FilePanel filePanel;

    public Decompiler(CodePanel codePanel, FilePanel filePanel) {
        this.codePanel = codePanel;
        this.filePanel = filePanel;
    }

    public abstract void openFile(File file) throws IOException;

    public abstract void closeFile();

    public abstract void selectFile(String name) throws IOException, ClassFileException;

    public abstract void save(File destination) throws FileNotFoundException;

    public abstract void saveAll(File destination) throws IOException, ClassFileException;

    public abstract boolean hasFile();

    public abstract String getFile();

    public abstract boolean hasSelectedFile();

    public abstract String getSelectedFile();

    public abstract String getSource(String name) throws IOException, ClassFileException;

    public abstract List<ClassFile> getClassFiles() throws IOException, ClassFileException;

}
