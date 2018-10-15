package com.elon.aop.advice;

import com.elon.core.proxy.advice.Advice;
import com.elon.core.proxy.anotation.AfterAdvice;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 2017/2/23 10:44.
 * <p>
 * Email: cheerUpPing@163.com
 */
@AfterAdvice(callBackVal = 1)
public class BeforeAdvice implements Advice {


    public void doAdvice(Object obj, Method method, Object[] args, MethodProxy proxy) {
        if (args != null){
            for (Object object : args){
                System.out.print(object);
            }
            System.out.print("\n");
        }
        System.out.println("前置通知................");
    }
}
