package com.mrsalwater.kapteyn.ui.decompiler;

import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeParser;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFileParser;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.ui.panel.code.CodePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.file.FilePanel;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ClassFileDecompiler extends Decompiler {

    private File file;
    private String name;
    private String source;
    private ClassFile classFile;

    public ClassFileDecompiler(CodePanel codePanel, FilePanel filePanel) {
        super(codePanel, filePanel);
    }

    @Override
    public void openFile(File file) throws IOException {
        this.file = file;
        this.name = null;
        this.source = null;

        filePanel.setFile(file);
    }

    @Override
    public void closeFile() {
        file = null;
        name = null;
        source = null;
        classFile = null;

        filePanel.clearFile();
        codePanel.setText("");
    }

    @Override
    public void selectFile(String name) throws IOException, ClassFileException {
        if (this.name != null && this.name.equals(name)) {
            return;
        }

        this.name = name;
        init();

        codePanel.setText(source);
    }

    @Override
    public void save(File destination) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(destination)) {
            out.print(source);
        }
    }

    @Override
    public void saveAll(File destination) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destination));

        String fileName = getFile().substring(0, getFile().lastIndexOf(".")).concat(".source");
        ZipEntry zipEntry = new ZipEntry(fileName);
        zip.putNextEntry(zipEntry);

        byte[] source = this.source.getBytes();
        zip.write(source, 0, source.length);
        zip.closeEntry();

        zip.close();
    }

    @Override
    public boolean hasFile() {
        return file != null;
    }

    @Override
    public String getFile() {
        return file.getName();
    }

    @Override
    public boolean hasSelectedFile() {
        return name != null;
    }

    @Override
    public String getSelectedFile() {
        return name;
    }

    @Override
    public String getSource(String name) {
        return source;
    }

    @Override
    public List<ClassFile> getClassFiles() {
        return Collections.singletonList(classFile);
    }

    private void init() throws IOException, ClassFileException {
        byte[] bytes = Files.readAllBytes(file.toPath());

        ClassFileParser classFileParser = new ClassFileParser(bytes);
        classFile = classFileParser.parse();

        ByteCodeParser byteCodeParser = new ByteCodeParser(classFile);
        source = byteCodeParser.parse().getSource();
    }

}
