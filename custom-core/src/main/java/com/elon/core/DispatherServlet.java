package com.elon.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        String contextPath = req.getContextPath();
        String requestUri = req.getRequestURI();
        String url = requestUri.substring(requestUri.indexOf(contextPath) + contextPath.length());
        String beanKey = WebApplication.urlBeanKey.get(url);
        if (beanKey == null) {
            throw new RuntimeException("404");
        }
        //执行拦截器
        for (AbstractInterceptor interceptor : WebApplication.interceptors) {
            if (!interceptor.handlerInterceptor(req, resp)) {
                return;
            }
        }
        //执行controller
        Object controller = WebApplication.beansMap.get(beanKey);
        Method method = WebApplication.urlMethod.get(url);
        try {
            Object object = method.invoke(controller, "自定义ioc容器!");
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
