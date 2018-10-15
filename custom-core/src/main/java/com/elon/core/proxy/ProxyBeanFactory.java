package com.elon.core.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 *
 * <p>
 *
 * <p>
 * 生成代理类
 */
public class ProxyBeanFactory {


    /**
     * 生成jdk动态代理类
     *
     * @param targetCls
     * @param jdkProxyCallBack
     * @return
     */
    public static Object newJDKProxyObj(Class targetCls, JDKProxyCallBack jdkProxyCallBack) {
        Object result = Proxy.newProxyInstance(targetCls.getClassLoader(), targetCls.getInterfaces(), jdkProxyCallBack);
        return result;
    }

    /**
     * 生成cglib动态代理类
     *
     * @param cls
     * @param cgLibProxyCallBack
     * @return
     */
    public static Object newCGLibProxyObj(Class cls, CGLibProxyCallBack cgLibProxyCallBack) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(cgLibProxyCallBack);
        Object result = enhancer.create();
        return result;
    }

}
