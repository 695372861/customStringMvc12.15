package com.elon.core;

import com.elon.core.anotation.*;
import com.elon.core.proxy.CGLibProxyCallBack;
import com.elon.core.proxy.JDKProxyCallBack;
import com.elon.core.proxy.ProxyBeanFactory;
import com.elon.core.proxy.advice.Advice;
import com.elon.core.proxy.advice.JDKAdvice;
import com.elon.core.proxy.anotation.*;
import com.elon.core.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * <p>
 * <p>
 * <p>
 * 上下文监听器
 */
public class ContextListener implements ServletContextListener {


    /**
     * 是实现ServletContextListener中的方法
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
        Log.info("上下文监听器启动!");
        //进行包扫描得到所有的类的完全限定名
        scanBasePackage("com.elon");
        //实例化,将所有的被@Controller和@Service注释的类进行实例化并且放入bean集合中
        instance();
        //处理url请求映射
        initMapping();
        //加载拦截器
        initInterceptor();
        //
        initAdice();
        initCGLibProxyAndCallBack();
        ioc();
        Log.info("容器基本信息......");
        Log.info("bean信息......");
        for (Map.Entry<String, Object> entry : WebApplication.beansMap.entrySet()) {
            Log.info(entry.getKey() + ":" + entry.getValue());
        }
        Log.info("类信息......");
        for (String className : WebApplication.classNames) {
            Log.info(className);
        }
        Log.info("拦截器信息......");
        for (AbstractInterceptor interceptor : WebApplication.interceptors) {
            Log.info(interceptor.toString());
        }
        Log.info("url和bean映射信息......");
        for (Map.Entry<String, String> entry : WebApplication.urlBeanKey.entrySet()) {
            Log.info(entry.getKey() + ":" + entry.getValue());
        }
        Log.info("url和method映射信息......");
        for (Map.Entry<String, Method> entry : WebApplication.urlMethod.entrySet()) {
            Log.info(entry.getKey() + ":" + entry.getValue());
        }
        Log.info("容器初始化完成......");

    }

    /**
     * 是实现ServletContextListener中的方法
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     */
    public void contextDestroyed(ServletContextEvent sce) {
        Log.info("上下文监听器销毁!");
    }

    /**
     * 扫描基包,获取所有的类名(全限定名)
     *
     * @param basePackage
     */
    private void scanBasePackage(String basePackage) {
        String basePackageStr = basePackage.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource("/");
        System.out.println("url.getPath=" + url.getPath());
        String basePath = url.getPath() + basePackageStr;
        System.out.println("basePath=" + basePath);
        File baseFile = new File(basePath);
        String[] fileNames = baseFile.list();
        for (String fileName : fileNames) {
            File file = new File(baseFile, fileName);
            if (file.isDirectory()) {
                scanBasePackage(basePackage + "." + fileName);
            } else if (file.isFile()) {
                if (fileName.endsWith(".class")) {
                    WebApplication.classNames.add(basePackage + "." + fileName.replaceAll("\\.class", ""));
                }
            }
        }
    }

    /**
     * 实例化,将所有的被@Controller和@Service注释的类进行实例化并且放入bean集合中
     */
    private void instance() {

        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                String beanName = classNameLower(className);
                if (cls.isAnnotationPresent(Controller.class)) {
                    Controller controller = (Controller) cls.getAnnotation(Controller.class);
                    String controllerName = controller.name();
                    controllerName = ("".equals(controllerName) ? beanName : controllerName);
                    Object object = cls.newInstance();
                    WebApplication.beansMap.put(controllerName, object);
                } else if (cls.isAnnotationPresent(Service.class)) {
                    Service service = (Service) cls.getAnnotation(Service.class);
                    String serviceName = service.name();
                    serviceName = ("".equals(serviceName) ? beanName : serviceName);
                    Object object = cls.newInstance();
                    WebApplication.beansMap.put(serviceName, object);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入字段
     */
    private void ioc() {
        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                if (cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Service.class) || cls.isAnnotationPresent(CGLibProxy.class) || cls.isAnnotationPresent(JDKProxy.class)) {
                    Field[] fields = cls.getDeclaredFields();
                    String objectName = null;
                    if (cls.isAnnotationPresent(Controller.class)) {
                        Controller controller = (Controller) cls.getAnnotation(Controller.class);
                        objectName = ("".equals(controller.name()) ? classNameLower(className) : controller.name());
                    } else if (cls.isAnnotationPresent(Service.class)) {
                        Service service = (Service) cls.getAnnotation(Service.class);
                        objectName = ("".equals(service.name()) ? classNameLower(className) : service.name());
                    } else if (cls.isAnnotationPresent(CGLibProxy.class)) {
                        CGLibProxy cgLibProxy = (CGLibProxy) cls.getAnnotation(CGLibProxy.class);
                        objectName = ("".equals(cgLibProxy.name()) ? classNameLower(className) : cgLibProxy.name());
                    } else if (cls.isAnnotationPresent(JDKProxy.class)) {
                        JDKProxy jdkProxy = (JDKProxy) cls.getAnnotation(JDKProxy.class);
                        objectName = ("".equals(jdkProxy.name()) ? classNameLower(className) : jdkProxy.name());
                    }
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(AutoWire.class)) {
                            field.setAccessible(true);
                            String fieldName = classNameLower(field.getType().getName());
                            AutoWire autoWire = field.getAnnotation(AutoWire.class);
                            String beanName = ("".equals(autoWire.name()) ? fieldName : autoWire.name());
                            field.set(WebApplication.beansMap.get(objectName), WebApplication.beansMap.get(beanName));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 处理url请求映射，将所有的Controller和其中的方法放入WebApplication中以供地址映射
     */
    public void initMapping() {
        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                if (cls.isAnnotationPresent(Controller.class)) {
                    Controller controller = (Controller) cls.getAnnotation(Controller.class);
                    String beanKey = ("".equals(controller.name()) ? classNameLower(className) : controller.name());
                    RequestMapping requestMapping = (RequestMapping) cls.getAnnotation(RequestMapping.class);
                    String baseUrl = (requestMapping == null ? "" : requestMapping.value());
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            requestMapping = method.getAnnotation(RequestMapping.class);
                            String allUrl = baseUrl + requestMapping.value();
                            WebApplication.urlBeanKey.put(allUrl, beanKey);
                            WebApplication.urlMethod.put(allUrl, method);


                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 实例化拦截器
     */
    private void initInterceptor() {
        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                if (cls.isAnnotationPresent(Interceptor.class)) {
                    //判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。
                    if (!AbstractInterceptor.class.isAssignableFrom(cls)) {
                        throw new RuntimeException(className + " not inherit abstract AbstractInterceptor");
                    }
                    Interceptor interceptor = (Interceptor) cls.getAnnotation(Interceptor.class);
                    Object object = cls.newInstance();
                    //得到父类中的order属性，即AbstractiInterceptor中的order属性
                    Field order = cls.getSuperclass().getDeclaredField("order");
                    //设置私有属性可以访问
                    order.setAccessible(true);
                    order.set(object, interceptor.order());
                    WebApplication.interceptors.add((AbstractInterceptor) object);
                    //对拦截器进行排序
                    Collections.sort(WebApplication.interceptors, new Comparator<AbstractInterceptor>() {
                        public int compare(AbstractInterceptor o1, AbstractInterceptor o2) {
                            return o1.getOrder() - o2.getOrder();
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 实例化 通知bean
     */
    public void initAdice() {
        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                Advice Advice = null;
                if (cls.isAnnotationPresent(BeforeAdvice.class) || cls.isAnnotationPresent(AfterAdvice.class) || cls.isAnnotationPresent(ThrowingAdvice.class)) {
                    if (!Advice.class.isAssignableFrom(cls)) {
                        throw new RuntimeException(className + " not inherit interface Advice!");
                    }
                    Advice = (Advice) cls.newInstance();
                }
                if (cls.isAnnotationPresent(BeforeAdvice.class)) {//前置通知
                    BeforeAdvice beforeAdvice = (BeforeAdvice) cls.getAnnotation(BeforeAdvice.class);
                    WebApplication.adviceBeansMap.put(WebApplication.beforeAdvice + beforeAdvice.callBackVal(), Advice);
                } else if (cls.isAnnotationPresent(AfterAdvice.class)) {//后置通知
                    AfterAdvice afterAdvice = (AfterAdvice) cls.getAnnotation(AfterAdvice.class);
                    WebApplication.adviceBeansMap.put(WebApplication.afterAdvice + afterAdvice.callBackVal(), Advice);
                } else if (cls.isAnnotationPresent(ThrowingAdvice.class)) {//异常通知
                    ThrowingAdvice throwingAdvice = (ThrowingAdvice) cls.getAnnotation(ThrowingAdvice.class);
                    WebApplication.adviceBeansMap.put(WebApplication.throwingAdvice + throwingAdvice.callBackVal(), Advice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 实例化 代理和回调
     */
    public void initCGLibProxyAndCallBack() {

        for (String className : WebApplication.classNames) {
            try {
                Class cls = Class.forName(className);
                if (cls.isAnnotationPresent(CGLibProxy.class)) {
                    CGLibProxy cgLibProxy = (CGLibProxy) cls.getAnnotation(CGLibProxy.class);
                    String beanName = ("".equals(cgLibProxy.name()) ? classNameLower(className) : cgLibProxy.name());
                    int callBackVal = cgLibProxy.callBackVal();
                    CGLibProxyCallBack cgLibProxyCallBack = new CGLibProxyCallBack(callBackVal);
                    WebApplication.callBacks.add(cgLibProxyCallBack);
                    Object proxyObj = ProxyBeanFactory.newCGLibProxyObj(cls, cgLibProxyCallBack);
                    WebApplication.beansMap.put(beanName, proxyObj);
                } else if (cls.isAnnotationPresent(JDKProxy.class)) {
                    JDKProxy jdkProxy = (JDKProxy) cls.getAnnotation(JDKProxy.class);
                    Class[] interfaces = cls.getInterfaces();
                    if (interfaces == null || interfaces.length == 0) {
                        throw new RuntimeException(className + " has not inherit interface,cannot use jdk proxy!");
                    }
                    Object object = cls.newInstance();
                    JDKProxyCallBack jdkProxyCallBack = new JDKProxyCallBack(object, jdkProxy.callBackVal());
                    Object proxy = ProxyBeanFactory.newJDKProxyObj(cls, jdkProxyCallBack);
                    for (Class ife : interfaces) {
                        WebApplication.beansMap.put(classNameLower(ife.getName()), proxy);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过类的全限定名 获取第一个字母小写的类名
     *
     * @param className
     * @return
     */
    private String classNameLower(String className) {
        String result = className;
        if (className.contains(".")) {
            result = className.substring(className.lastIndexOf(".") + 1);
        }
        return fistLower(result);
    }

    /**
     * 第一个字母小写
     *
     * @param str
     * @return
     */
    private String fistLower(String str) {
        String first = str.substring(0, 1);
        String left = str.substring(1);
        String result = first.toLowerCase() + left;
        return result;
    }

}