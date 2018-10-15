package com.elon.core.util;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckMethodAdapter extends MethodAdapter {
    public AddSecurityCheckMethodAdapter(MethodVisitor mv) {
        super(mv);
    }

    @Override
    public void visitCode() {
        visitMethodInsn(Opcodes.INVOKESTATIC, SecurityChecker.class.getName(),
                "checkSecurity", "()V");
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }
}
