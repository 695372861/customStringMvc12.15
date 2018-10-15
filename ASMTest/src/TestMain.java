import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;

import com.sun.org.apache.bcel.internal.generic.ILOAD;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
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
        TestClassWriter();
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
        //手动计算栈帧大小、本地变量和操作数栈的大小；COMPUTE.MAXS需要自己计算栈帧大小，但本地变量与操作数已自动计算好，当然也可以调用visitMaxs方法，只不过不起作用，参数会被忽略；
        // COMPUTE_FRAMES栈帧本地变量和操作数栈都自动计算，不需要调用visitFrame和visitMaxs方法，即使调用也会被忽略
        ClassWriter classWriter=new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //定义一个接口并且指定的名称和要实现的接口
        classWriter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,clazzName,null, "java/lang/Object",new String[]{"InterfaceA"});//
        //在接口中添加变量
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"LESS","I",null,-11).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"EQUAL","I",null,10).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"GREATER","I",null,11).visitEnd();
        classWriter.visitField(Opcodes.ACC_PRIVATE,"name","Ljava/lang/String;",null,null).visitEnd();
        classWriter.visitField(Opcodes.ACC_PRIVATE,"age","I",null,0).visitEnd();

        //在接口中定义一个无参数的构造方法
        MethodVisitor constructor=classWriter.visitMethod(Opcodes.ACC_PUBLIC,"<init>","()V",null,null);
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        //执行父类的init初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //从当前方法返回void
        constructor.visitInsn(Opcodes.RETURN);
        //设置本地变量与操作数
        constructor.visitMaxs(0, 0);
        //constructor.visitFrame(1,1)//可以指定栈帧中的本地变量与操作数。
        constructor.visitEnd();


        constructor=classWriter.visitMethod(Opcodes.ACC_PUBLIC,"compareTo","(II)I",null,null);
        //开始生成一个方法
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ILOAD,1);
        constructor.visitVarInsn(Opcodes.ILOAD,2);
        constructor.visitInsn(Opcodes.IADD);
        constructor.visitInsn(Opcodes.IRETURN);
       // 操作数栈大小为2，局部变量表大小为3
        constructor.visitMaxs(2,3);
        //结束生产一个方法
        constructor.visitEnd();


        constructor=classWriter.visitMethod(Opcodes.ACC_PUBLIC,"compareTo2","(I)V",null,null);
        //开始生成一个方法
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitVarInsn(Opcodes.ILOAD, 1);
        constructor.visitFieldInsn(Opcodes.PUTFIELD, "InterfaceB", "age", "I");
//        constructor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
//        constructor.visitLdcInsn("ssss");
//        constructor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V", false);
        constructor.visitInsn(Opcodes.RETURN);
        // 操作数栈大小为2，局部变量表大小为3
        constructor.visitMaxs(0,0);
        //结束生产一个方法
        constructor.visitEnd();

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
            Method method=clazz.getDeclaredMethod("compareTo",int.class,int.class);
            Object obj=method.invoke(object,1,2);
            System.out.println(obj.toString());
            Method method1=clazz.getDeclaredMethod("compareTo2",int.class);
            method1.invoke(object,17);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
    //visitInsn、visitVarInsn、visitMethodInsn等以Insn结尾的方法可以添加方法实现的字节码。
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