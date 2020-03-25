package com.mrsalwater.kapteyn.ui.finder.result;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.method.Method;

public class FindResultLocalVariable extends FindResult<Method> {

    public FindResultLocalVariable(ClassFile classFile, String string, Method method) {
        super(classFile, string, method);
    }

}
