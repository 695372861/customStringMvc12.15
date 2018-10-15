package com.elon.core.util;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class DelLoginCLassAdapter extends ClassAdapter {
    public DelLoginCLassAdapter(ClassVisitor classVisitor) {
        super(classVisitor);
    }

    @Override
    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {

        return super.visitField(i, s, s1, s2, o);
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {

        MethodVisitor mv=super.visitMethod(i, s, s1, s2, strings);
        MethodVisitor wrappedMv = mv;
        if (mv != null) {
            // 对于 "operation" 方法
            if (s.equals("infof")) {
                // 使用自定义 MethodVisitor，实际改写方法内容
                wrappedMv = new Ext(mv);
            }
        }

        return wrappedMv;
    }
}
