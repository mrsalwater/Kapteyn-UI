package com.mrsalwater.kapteyn.ui.decompiler;

import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeFile;
import com.mrsalwater.kapteyn.decompiler.bytecode.ByteCodeParser;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.ClassFileParser;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.ui.panel.code.CodePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.file.FilePanel;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class JarFileDecompiler extends Decompiler {

    private File file;
    private String name;
    private String source;

    private List<ClassFile> classFiles = new ArrayList<>();

    public JarFileDecompiler(CodePanel codePanel, FilePanel filePanel) {
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

        filePanel.clearFile();
        codePanel.setText("");
    }

    @Override
    public void selectFile(String name) throws IOException, ClassFileException {
        if (this.name != null && this.name.equals(name)) {
            return;
        }

        this.name = name;
        this.source = getSource(name);

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
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destination));

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.getName().contains(".")) {
                String jarEntryExtension = jarEntry.getName().substring(jarEntry.getName().lastIndexOf(".") + 1);

                if (jarEntryExtension.equals("class")) {
                    String jarEntryName = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf(".") + 1);
                    byte[] bytes = toByteArray(jarFile.getInputStream(jarEntry), (int) jarEntry.getCompressedSize());

                    byte[] source;
                    ZipEntry zipEntry;
                    try {
                        ClassFileParser classFileParser = new ClassFileParser(bytes);
                        ClassFile classFile = classFileParser.parse();

                        ByteCodeParser byteCodeParser = new ByteCodeParser(classFile);
                        ByteCodeFile byteCodeFile = byteCodeParser.parse();

                        source = byteCodeFile.getSource().getBytes();
                        zipEntry = new ZipEntry(jarEntryName.concat("source"));
                    } catch (Exception e) {
                        source = bytes;
                        zipEntry = new ZipEntry(jarEntryName.concat(".class"));
                    }

                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(source, 0, source.length);
                    zipOutputStream.closeEntry();
                }
            }
        }

        zipOutputStream.close();
        jarFile.close();
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
        return getName(name);
    }

    @Override
    public String getSource(String name) throws IOException, ClassFileException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.getName().replace("/", ".").equals(name)) {
                byte[] bytes = toByteArray(jarFile.getInputStream(jarEntry), (int) jarEntry.getCompressedSize());

                ClassFileParser classFileParser = new ClassFileParser(bytes);
                ClassFile classFile = classFileParser.parse();

                ByteCodeParser byteCodeParser = new ByteCodeParser(classFile);
                ByteCodeFile byteCodeFile = byteCodeParser.parse();

                return byteCodeFile.getSource();
            }
        }

        jarFile.close();
        throw new NullPointerException();
    }

    @Override
    public List<ClassFile> getClassFiles() throws IOException {
        if (classFiles.isEmpty()) {
            initClassFiles();
        }

        return classFiles;
    }

    private void initClassFiles() throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (jarEntry.getName().endsWith(".class")) {
                byte[] bytes = toByteArray(jarFile.getInputStream(jarEntry), (int) jarEntry.getCompressedSize());

                try {
                    ClassFileParser classFileParser = new ClassFileParser(bytes);
                    ClassFile classFile = classFileParser.parse();
                    classFiles.add(classFile);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }

        jarFile.close();
    }

    private String getName(String string) {
        boolean extension = true;
        char[] c = string.toCharArray();

        for (int i = string.length() - 1; i > 0; i--) {
            if (c[i] == '.') {
                if (extension) {
                    extension = false;
                } else {
                    return string.substring(i + 1);
                }
            }
        }

        return string;
    }

    private byte[] toByteArray(InputStream inputStream, int size) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[size];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        return byteArrayOutputStream.toByteArray();
    }

}
