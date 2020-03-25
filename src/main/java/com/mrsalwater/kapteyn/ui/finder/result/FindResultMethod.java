package com.mrsalwater.kapteyn.ui.finder.result;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.method.Method;

public final class FindResultMethod extends FindResult<Method> {

    public FindResultMethod(ClassFile classFile, String string, Method method) {
        super(classFile, string, method);
    }

}
