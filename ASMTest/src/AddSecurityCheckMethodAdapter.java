
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckMethodAdapter extends MethodVisitor {
    public AddSecurityCheckMethodAdapter(MethodVisitor mv) {
        super(Opcodes.ASM5,mv);
    }

    /**
     *
     *  version :jdk版本
     *  access  pulbic,...staitc,final, ACC_ABSTRACT：抽象类 ,ACC_INTERFACE:接口,ACC_ANNOTATION:注解,ACC_ENUM:枚举
     *  name 类名,注意packagename 以 java/lang/String 形式。
     *  signature   ---泛型信息
     *  superName  父类
     *  interfaces 接口数组
     */
    //在方法开始的时候执行
    @Override
    public void visitCode() {
        //插入一个方法,desc表示返回值类型()V表示空
        visitMethodInsn(Opcodes.INVOKESTATIC, SecurityChecker.class.getName(),
                "checkSecurity", "()V");
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }
}
