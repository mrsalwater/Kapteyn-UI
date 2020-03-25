package com.mrsalwater.kapteyn.ui.finder.result;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.constantpool.ConstantString;

public final class FindResultLDC extends FindResult<ConstantString> {

    public FindResultLDC(ClassFile classFile, String string, ConstantString constantString) {
        super(classFile, string, constantString);
    }

}
