package com.elon.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 *
 * <p>
 * 控制器转发
 */
public class DispatherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求的request的字符编码形式
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        //
        String contextPath = req.getContextPath();
        //得到工程后面参数前面的路径例如http://localhost:8080/elon/sayHello?param=123
        //得到的路径名称为/elon/sayHello
        String requestUri = req.getRequestURI();
        String url = requestUri.substring(requestUri.indexOf(contextPath) + contextPath.length());
        //从注册的Bean中的查找所需要的bean即注册的类例如ElonController
        String beanKey = WebApplication.urlBeanKey.get(url);
        System.out.println("contextPath:"+contextPath+"  requestUri="+requestUri+" url="+url+" beanKey="+beanKey);
        if (beanKey == null) {
            throw new RuntimeException("404");
        }
        //执行拦截器
        for (AbstractInterceptor interceptor : WebApplication.interceptors) {
            //看是否通过过滤
            if (!interceptor.handlerInterceptor(req, resp)) {
                return;
            }
        }
        //根据调用的信息将要使用的bean取出
        Object controller = WebApplication.beansMap.get(beanKey);
        Method method = WebApplication.urlMethod.get(url);
        try {
            //在java1.8以后才可以使用这个方法，并且在编译的时候要设置javac的编译参数-parameters
            Parameter[] parameter=method.getParameters();
            List<Object> list=new ArrayList<>();
            System.out.println("Method.getParameters.length:"+parameter.length);
            //设置对应方法的参数，只支持基础类型string,int,double的和httpservletrequest,httpservletrequest参数类型的设置
            for (int i=0;i<parameter.length;i++) {
                String value=req.getParameter(parameter[i].getName());
                System.out.println("parameter="+parameter[i].getName());
                Class clazz=parameter[i].getType();
                System.out.println("clazz.type="+clazz.getTypeName());
                if(value==null||value=="")
                {
                    if(clazz.getTypeName().equals("javax.servlet.http.HttpServletRequest") )
                    {
                        list.add(req);
                    }else if(clazz.getTypeName().equals("avax.servlet.http.HttpServletResponse"))
                    {
                        list.add(resp);
                    }else if (clazz.getTypeName().equals("int"))
                    {
                        list.add(0);
                    }else if(clazz.getTypeName().equals("double"))
                    {
                        list.add(0);
                    }else {
                        list.add(null);
                    }
                }else
                {
                    if(clazz.getTypeName().equals("javax.servlet.http.HttpServletRequest") )
                    {
                        list.add(req);
                    }else if(clazz.getTypeName().equals("avax.servlet.http.HttpServletResponse"))
                    {
                        list.add(resp);
                    }else if (clazz.getTypeName().equals("int"))
                    {
                        list.add(Integer.parseInt(value));
                    }else if(clazz.getTypeName().equals("double"))
                    {
                        list.add(Double.parseDouble(value));
                    }else {
                        list.add(value);
                    }
                }

            }
            //执行controller中的方法
            Object object = method.invoke(controller, list.toArray());

            PrintWriter printWriter = resp.getWriter();
            printWriter.write(object + "");
            printWriter.flush();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
