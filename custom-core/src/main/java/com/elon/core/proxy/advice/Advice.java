package com.elon.core.proxy.advice;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * <p>
 *
 */
public interface Advice {

    /**
     * 发送通知
     */
    void doAdvice(Object obj, Method method, Object[] args, MethodProxy proxy);

}
