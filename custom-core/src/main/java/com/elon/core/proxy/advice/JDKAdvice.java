package com.elon.core.proxy.advice;

import java.lang.reflect.Method;

/**
 *
 * <p>
 *
 */
public interface JDKAdvice {

    /**
     * 发送通知
     */
    void doAdvice(Object proxy, Method method, Object[] args);

}
