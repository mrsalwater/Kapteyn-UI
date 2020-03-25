package com.mrsalwater.kapteyn.ui.decompiler;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;

import java.io.File;
import java.util.List;

public final class EmptyDecompiler extends Decompiler {

    public EmptyDecompiler() {
        super(null, null);
    }

    @Override
    public void openFile(File file) {

    }

    @Override
    public void closeFile() {

    }

    @Override
    public void selectFile(String name) {

    }

    @Override
    public void save(File destination) {

    }

    @Override
    public void saveAll(File destination) {

    }

    @Override
    public boolean hasFile() {
        return false;
    }

    @Override
    public String getFile() {
        return null;
    }

    @Override
    public boolean hasSelectedFile() {
        return false;
    }

    @Override
    public String getSelectedFile() {
        return null;
    }

    @Override
    public String getSource(String name) {
        return null;
    }

    @Override
    public List<ClassFile> getClassFiles() {
        return null;
    }

}
