package com.mrsalwater.kapteyn.ui.finder.result;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.field.Field;

public final class FindResultField extends FindResult<Field> {

    public FindResultField(ClassFile classFile, String string, Field field) {
        super(classFile, string, field);
    }

}
