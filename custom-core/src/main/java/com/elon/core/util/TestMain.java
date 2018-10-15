package com.elon.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.objectweb.asm.*;

public class TestMain {

    public static void main(String[] arge)
    {
        try {
            ClassReader cr=new ClassReader(Account.class.getName());
                ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassAdapter classAdapter=new AddSecurityCheckClassAdapter(cw);
            cr.accept(classAdapter,ClassReader.SKIP_DEBUG);
            byte[] data = cw.toByteArray();
            File file = new File("F:\\customSpringMvc\\custom-core\\target\\classes\\com\\elon\\core\\util\\Account.class");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Account account=new Account();
        account.operation();
    }

    //执行某个类中的某个方法
    public static void execMethod(Class cla,String mName,Object... param)
    {
        try {

            Method method=cla.getDeclaredMethod(mName,param.getClass());
            //如果有返回值可以用Object来接收method.invoke的返回值
            method.invoke(cla.newInstance(),param);
            //如果调用的是静态方法第一个参数设置为null即可
            //method.invoke(null,param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}