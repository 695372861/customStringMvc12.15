
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

//在分析一个已经存在的类的时候，唯一必须的主件是ClassReader，
// 例子，打印一个类的内容，类似简化的javap
public class ClassPrinter extends ClassVisitor {

    public ClassPrinter() {
        super(Opcodes.ASM5);
    }



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        System.out.println(name+"extends"+superName+"{");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println(" "+desc+" "+name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println(" "+name+desc);
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }
}
