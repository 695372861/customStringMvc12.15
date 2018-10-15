package com.elon.interceptor;


import com.elon.core.AbstractInterceptor;
import com.elon.core.anotation.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2017/2/20 16:29.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Interceptor(order = 1)
public class ElonInterceptor extends AbstractInterceptor {

    public boolean handlerInterceptor(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("类名：" + this.hashCode() + this.getClass().getName());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        return true;
    }
}
