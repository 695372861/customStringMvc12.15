package com.elon.core;

import com.elon.core.proxy.advice.Advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 *
 */
public interface WebApplication {

    /**
     * 保存项目类的全限定名
     */
    List<String> classNames = new ArrayList<String>();

    /**
     * 保存@controller和@service的bean
     */
    Map<String, Object> beansMap = new HashMap<String, Object>();

    /**
     * 请求url和beansMap的key关系
     */
    Map<String, String> urlBeanKey = new HashMap<String, String>();

    /**
     * 请求url和method关系
     */
    Map<String, Method> urlMethod = new HashMap<String, Method>();

    /**
     * 保存拦截器
     */
    List<AbstractInterceptor> interceptors = new ArrayList<AbstractInterceptor>();

    /**
     * 保存通知bean(前置通知 后置通知 异常通知)
     */
    Map<String,Advice> adviceBeansMap = new HashMap<String, Advice>();

    /**
     * 前置通知bean  map的前缀
     */
    String beforeAdvice = "before_advice_";

    /**
     * 后置通知bean map的前缀
     */
    String afterAdvice = "after_advice_";

    /**
     * 异常通知bean map的前缀
     */
    String throwingAdvice = "throwing_advice_";

    /**
     * 存放jdk回调和cglib回调
     */
    List callBacks = new ArrayList();

}
