
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckClassAdapter extends ClassVisitor {
    String enhancedSuperName;
    public AddSecurityCheckClassAdapter(String enhancedSuperName,ClassVisitor cv) {
        //Responsechain 的下一个 ClassVisitor，这里我们将传入 ClassWriter，
        // 负责改写后代码的输出
        super(Opcodes.ASM5,cv);
        this.enhancedSuperName=enhancedSuperName;
    }

    // 重写 visitMethod，访问到 "operation" 方法时，
    // 给出自定义 MethodVisitor，实际改写方法内容
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        MethodVisitor wrappedMv = mv;
        if (mv != null) {
            if (name.equals("operation")) {
                wrappedMv = new AddSecurityCheckMethodAdapter(mv);
            } else if (name.equals("<init>")) {
                wrappedMv = new ChangeToChildConstructorMethodAdapter(mv,
                        enhancedSuperName);//enhancedSuperName
            }
        }
        return wrappedMv;

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String enhancedName = name + "$EnhancedByASM";  // 改变类命名
        enhancedSuperName = name; // 改变父类，这里是”Account”
        super.visit(version, access, enhancedName, signature,
                enhancedSuperName, interfaces);


    }
}
