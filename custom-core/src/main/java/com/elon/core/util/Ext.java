package com.elon.core.util;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Ext extends MethodAdapter {
    public Ext(MethodVisitor mv) {
        super(mv);
    }

    public void visitCode() {
        visitMethodInsn(Opcodes.INVOKESTATIC, "com.elon.core.util.Info",
                "say", "()V");
    }


}
