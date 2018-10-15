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
        TestClassWriter();
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
        ClassWriter classWriter=new ClassWriter(0);
        //定义一个接口并且指定的名称和要实现的接口
        classWriter.visit(Opcodes.V1_7,Opcodes.ACC_PUBLIC,clazzName,null,
                "java/lang/Object",null);//new String[]{"java/lang/Runnable"}
        //在接口中添加变量
        classWriter.visitField(Opcodes.ACC_PUBLIC,"LESS","I",null,new Integer(-1)).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC,"EQUAL","I",null,new Integer(0)).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC,"GREATER","I",null,new Integer(1)).visitEnd();

        //在接口中定义一个方法
        classWriter.visitMethod(Opcodes.ACC_PUBLIC,"compareTo","(Ljava/lang/Object;Ljava/lang/Object;)V",null,null).visitEnd();
//        classWriter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,"main","(Ljava/lang/Object;)V",null,null).visitEnd();
        //类结束生产
        classWriter.visitEnd();
        byte[] data=classWriter.toByteArray();
        TestMain tm=new TestMain();
        String str=tm.getClass().getClassLoader().getResource("").getPath();
        try {
            FileOutputStream fos=new FileOutputStream(str+"/"+clazzName+".class");
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readerClass("InterfaceB");
        readerClass("InterfaceA");
        MyClassLoader myClassLoader=new MyClassLoader(str+"/InterfaceB.class");
        Class<?> clazz=null;
        try {
            if(null!=Class.forName("InterfaceB"))
            {
                System.out.println("Inser---------------史诗----------------");
            }
            clazz=Class.forName("InterfaceB");
            Field field=clazz.getField("LESS");
            field.setAccessible(true);
            System.out.println(field.getName()+"="+field.get(null).toString());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}