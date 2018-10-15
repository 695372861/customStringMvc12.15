package com.elon.core.proxy;

import com.elon.core.WebApplication;
import com.elon.core.proxy.advice.Advice;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * <p>
 *
 * <p>
 * cglib动态代理(这里可以理解为回调)
 */
public class CGLibProxyCallBack implements MethodInterceptor {


    private int callBackVal = 0;

    public CGLibProxyCallBack(int callBackVal) {

        this.callBackVal = callBackVal;

    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        //前置通知 -- 执行
        String beforeAdviceKey = WebApplication.beforeAdvice + callBackVal;
        Advice beforeAdvice = WebApplication.adviceBeansMap.get(beforeAdviceKey);
        if (beforeAdvice != null) {
            beforeAdvice.doAdvice(obj, method, args, proxy);
        }
        Object result = null;

        try {
            result = proxy.invokeSuper(obj, args);
            //后置通知
            String afterAdviceKey = WebApplication.afterAdvice + callBackVal;
            Advice afterAdvice = WebApplication.adviceBeansMap.get(afterAdviceKey);
            if (afterAdvice != null) {
                afterAdvice.doAdvice(obj, method, args, proxy);
            }
        } catch (Exception e) {
            //异常通知
            String throwingAdviceKey = WebApplication.throwingAdvice + callBackVal;
            Advice throwingAdvice = WebApplication.adviceBeansMap.get(throwingAdviceKey);
            if (throwingAdvice != null) {
                throwingAdvice.doAdvice(obj, method, args, proxy);
            }
        }
        return result;
    }
}
