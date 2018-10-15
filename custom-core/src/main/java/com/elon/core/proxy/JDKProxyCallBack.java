package com.elon.core.proxy;

import com.elon.core.WebApplication;
import com.elon.core.proxy.advice.Advice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * <p>
 *
 * <p>
 * jdk动态代理(这里可以理解为回调)
 */
public class JDKProxyCallBack implements InvocationHandler {

    private int callBackVal = 0;

    private Object targetObject;

    public JDKProxyCallBack(Object targetObject, int callBackVal) {
        this.targetObject = targetObject;
        this.callBackVal = callBackVal;
    }

    //类似回调函数的功能
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //前置通知
        //前置通知 -- 执行
        String beforeAdviceKey = WebApplication.beforeAdvice + callBackVal;
        Advice beforeAdvice = WebApplication.adviceBeansMap.get(beforeAdviceKey);
        if (beforeAdvice != null) {
            beforeAdvice.doAdvice(proxy, method, args, null);
        }

        //调用目标方法
        Object result = null;
        try {
            result = method.invoke(targetObject, args);
            //后置通知
            String afterAdviceKey = WebApplication.afterAdvice + callBackVal;
            Advice afterAdvice = WebApplication.adviceBeansMap.get(afterAdviceKey);
            if (afterAdvice != null) {
                afterAdvice.doAdvice(proxy, method, args, null);
            }

        } catch (Exception e) {
            //异常通知
            String throwingAdviceKey = WebApplication.throwingAdvice + callBackVal;
            Advice throwingAdvice = WebApplication.adviceBeansMap.get(throwingAdviceKey);
            if (throwingAdvice != null) {
                throwingAdvice.doAdvice(proxy, method, args, null);
            }

        }
        return result;
    }
}
