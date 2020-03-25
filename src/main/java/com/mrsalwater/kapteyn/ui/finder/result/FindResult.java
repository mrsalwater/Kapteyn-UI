package com.mrsalwater.kapteyn.ui.finder.result;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;

public abstract class FindResult<T> {

    private final ClassFile classFile;
    private final String string;
    private final T t;

    public FindResult(ClassFile classFile, String string, T t) {
        this.classFile = classFile;
        this.string = string;
        this.t = t;
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public String getString() {
        return string;
    }

}
