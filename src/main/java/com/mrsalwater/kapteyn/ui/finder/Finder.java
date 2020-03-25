package com.mrsalwater.kapteyn.ui.finder;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.attribute.AttributeCode;
import com.mrsalwater.kapteyn.decompiler.classfile.attribute.AttributeLocalVariableTable;
import com.mrsalwater.kapteyn.decompiler.classfile.constantpool.Constant;
import com.mrsalwater.kapteyn.decompiler.classfile.constantpool.ConstantPool;
import com.mrsalwater.kapteyn.decompiler.classfile.constantpool.ConstantString;
import com.mrsalwater.kapteyn.decompiler.classfile.field.Field;
import com.mrsalwater.kapteyn.decompiler.classfile.method.Method;
import com.mrsalwater.kapteyn.decompiler.exception.CorruptClassFileException;
import com.mrsalwater.kapteyn.ui.finder.result.*;

import java.util.ArrayList;
import java.util.List;

public final class Finder {

    private final List<ClassFile> classFiles;

    public Finder(List<ClassFile> classFiles) {
        this.classFiles = classFiles;
    }

    public List<FindResult<?>> find(FindOperation operation, String string) throws CorruptClassFileException {
        List<FindResult<?>> results = new ArrayList<>();

        for (ClassFile classFile : classFiles) {
            switch (operation) {
                case ALL:
                    results.addAll(findLDC(classFile, string));
                    results.addAll(findLocalVariable(classFile, string));
                    results.addAll(findMethodReference(classFile, string));
                    results.addAll(findFieldReference(classFile, string));
                    break;
                case LDC:
                    results.addAll(findLDC(classFile, string));
                    break;
                case LOCAL_VARIABLE:
                    results.addAll(findLocalVariable(classFile, string));
                    break;
                case METHOD_REFERENCE:
                    results.addAll(findMethodReference(classFile, string));
                    break;
                case FIELD_REFERENCE:
                    results.addAll(findFieldReference(classFile, string));
                    break;
            }
        }

        return results;
    }

    private List<FindResultLDC> findLDC(ClassFile classFile, String string) throws CorruptClassFileException {
        List<FindResultLDC> results = new ArrayList<>();

        ConstantPool constantPool = classFile.getConstantPool();
        for (Constant constant : constantPool.getConstants()) {
            if (constant instanceof ConstantString) {
                ConstantString constantString = (ConstantString) constant;
                String value = constantPool.getUTF8(constantString.getStringIndex()).getValue();

                if (value.equalsIgnoreCase(string)) {
                    results.add(new FindResultLDC(classFile, value, constantString));
                }
            }
        }

        return results;
    }

    private List<FindResultLocalVariable> findLocalVariable(ClassFile classFile, String string) throws CorruptClassFileException {
        List<FindResultLocalVariable> results = new ArrayList<>();

        ConstantPool constantPool = classFile.getConstantPool();
        for (Method method : classFile.getMethods()) {
            if (method.getAttributes().has(AttributeCode.class)) {
                AttributeCode attributeCode = method.getAttributes().get(AttributeCode.class);

                if (attributeCode.getAttributes().has(AttributeLocalVariableTable.class)) {
                    AttributeLocalVariableTable localVariableTable = attributeCode.getAttributes().get(AttributeLocalVariableTable.class);

                    for (AttributeLocalVariableTable.LocalVariable localVariable : localVariableTable.getLocalVariableTable()) {
                        String value = constantPool.getUTF8(localVariable.getNameIndex()).getValue();

                        if (value.equalsIgnoreCase(string)) {
                            results.add(new FindResultLocalVariable(classFile, value, method));
                        }
                    }
                }
            }
        }

        return results;
    }

    private List<FindResultMethod> findMethodReference(ClassFile classFile, String string) throws CorruptClassFileException {
        List<FindResultMethod> results = new ArrayList<>();

        ConstantPool constantPool = classFile.getConstantPool();
        for (Method method : classFile.getMethods()) {
            String value = constantPool.getUTF8(method.getNameIndex()).getValue();

            if (value.equalsIgnoreCase(string)) {
                results.add(new FindResultMethod(classFile, value, method));
            }
        }

        return results;
    }

    private List<FindResultField> findFieldReference(ClassFile classFile, String string) throws CorruptClassFileException {
        List<FindResultField> results = new ArrayList<>();

        ConstantPool constantPool = classFile.getConstantPool();
        for (Field field : classFile.getFields()) {
            String value = constantPool.getUTF8(field.getNameIndex()).getValue();

            if (value.equalsIgnoreCase(string)) {
                results.add(new FindResultField(classFile, value, field));
            }
        }

        return results;
    }

}
