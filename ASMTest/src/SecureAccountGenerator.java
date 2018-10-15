

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecureAccountGenerator {
    private static AccountGeneratorClassLoader classLoader =
            new AccountGeneratorClassLoader();

    private static Class secureAccountClass;

    public Account generateSecureAccount() throws ClassFormatError,
            InstantiationException, IllegalAccessException {
        if (null == secureAccountClass) {
            ClassReader cr = null;
            try {
                cr = new ClassReader("Account");
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassVisitor classAdapter = new AddSecurityCheckClassAdapter("Account",cw);
                cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
                byte[] data = cw.toByteArray();
                //将类文件输出到文件
                String str=this.getClass().getClassLoader().getResource("").getPath();
                File file = new File(str+"/Account$EnhancedByASM.class");
                FileOutputStream fout = new FileOutputStream(file);
                fout.write(data);
                fout.close();
                secureAccountClass = classLoader.defineClassFromClassFile(
                        "Account$EnhancedByASM",data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return (Account) secureAccountClass.newInstance();
    }

    public static class AccountGeneratorClassLoader extends ClassLoader {
        public Class defineClassFromClassFile(String className,
                                              byte[] classFile) throws ClassFormatError {
            return defineClass(className, classFile, 0, classFile.length);
        }
    }

}
