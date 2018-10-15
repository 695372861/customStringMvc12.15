import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;

import org.objectweb.asm.*;

public class TestMain {


    public static void main(String[] arge)
    {
//        TestMain tm=new TestMain();
//        tm.aop();
//        tm.exc("Account$EnhancedByASM");
//       SecureAccountGenerator sag=new SecureAccountGenerator();
//        try {
//            Account account=sag.generateSecureAccount();
//            account.operation();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        readerClass("java.lang.String");
//        TestClassWriter();
        createRun();
    }
    public  void exc(String className)
    {
        String str=this.getClass().getClassLoader().getResource("").getPath();
        MyClassLoader my=new MyClassLoader(str+className+".class");
        Class<?> clazz=null;
        try {
            clazz=Class.forName(className,true,my);
            Object object=clazz.newInstance();
            if (object instanceof Account)
            {
                Account account=(Account)object;
                account.operation();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
//        Account account=new Account();
//        account.operation();
    }
    public  void aop()
    {
        try {
            ClassReader cr=new ClassReader(Account.class.getName());
            ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classAdapter=new AddSecurityCheckClassAdapter("Account",cw);
            cr.accept(classAdapter,ClassReader.SKIP_DEBUG);
            byte[] data = cw.toByteArray();
//            String str = new TestMain().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//            String str=this.getClass().getResource("").getPath();
            String  str=this.getClass().getClassLoader().getResource("").getPath();
//            System.out.println(str+"Account.class");
            File file = new File(str+"/Account$EnhancedByASM.class");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readerClass(String className)
    {
        if(className==null || className=="")
        {
            className="java/lang/String";
        }
        try {
            //使用类的完全限定名来装载这个类，也还有其他的构造方法，例如使用文件流，
            ClassReader cr=new ClassReader(className);
            cr.accept(new ClassPrinter(),0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //使用ClassWriter来生产一个类
    public static void TestClassWriter()
    {
        String clazzName="InterfaceB";
        ClassWriter classWriter=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //定义一个接口并且指定的名称和要实现的接口
        classWriter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,clazzName,null, "java/lang/Object",null);//new String[]{"java/lang/Runnable"}
        //在接口中添加变量
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"LESS","I",null,-1).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"EQUAL","I",null,0).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"GREATER","I",null,1).visitEnd();

        //在接口中定义一个无参数的构造方法
        MethodVisitor constructor=classWriter.visitMethod(Opcodes.ACC_PUBLIC,"<init>","()V",null,null);
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        //执行父类的init初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //从当前方法返回void
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

//        classWriter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;Ljava/lang/Object;)V",null,null).visitEnd();
//        classWriter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"main","(Ljava/lang/Object;)V",null,null).visitEnd();
        //类结束生产
        classWriter.visitEnd();
        byte[] data=classWriter.toByteArray();
        TestMain tm=new TestMain();
        String str=tm.getClass().getClassLoader().getResource("").getPath();
        outClass(data,str+"/"+clazzName+".class");
        readerClass("InterfaceB");
        readerClass("InterfaceA");
        MyClassLoader myClassLoader=new MyClassLoader(str+"/InterfaceB.class");
        Class<?> clazz=null;
        try {
            if(null!=Class.forName(clazzName))
            {
                System.out.println("Inser---------------史诗----------------");
            }
            clazz=Class.forName("InterfaceB");
            Object object=clazz.newInstance();
            Field field=clazz.getDeclaredField("LESS");

            System.out.println(field.getName()+"="+field.get(object));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    static void outClass(byte[] data,String className)
    {
        try {
            FileOutputStream fos=new FileOutputStream(className);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static byte[] createVoidMethod(String className, String message) throws Exception
    {
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test
        ClassWriter cw = createClassWriter(className.replace('.', '/'));

        //创建run方法
        //()V表示函数，无参数，无返回值
        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        //先获取一个java.io.PrintStream对象
        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
        runMethod.visitLdcInsn(message);
        //执行println方法（执行的是参数为字符串，无返回值的println函数）
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1, 1);
        runMethod.visitEnd();
        return cw.toByteArray();
    }
    static ClassWriter createClassWriter(String className)
    {
        ClassWriter cw =new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,className,null,"java/lang/Object",null);
        MethodVisitor constructor=cw.visitMethod(Opcodes.ACC_PUBLIC,"<init>","()V",null,null);
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,"java/lang/Object","<init>","()V",false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1,1);
        constructor.visitEnd();
        return  cw;
    }

    public static void createRun()
    {
        TestMain tm=new TestMain();
        String str=tm.getClass().getClassLoader().getResource("").getPath();
        try {
            byte[] data=createVoidMethod("MyRun","Sys.out.print");
            outClass(data,str+"MyRun.class");
            MyClassLoader myClassLoader=new MyClassLoader(str+"MyRun.class");
            Class<?> clazz=Class.forName("MyRun",true,myClassLoader);
            clazz.getDeclaredMethods()[0].invoke(clazz.newInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}