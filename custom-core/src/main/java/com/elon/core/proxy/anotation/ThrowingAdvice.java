package com.elon.core.proxy.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * <p>
 *
 *
 * 异常通知 标记
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThrowingAdvice {

    int callBackVal() default 0;//关联回调函数

}
