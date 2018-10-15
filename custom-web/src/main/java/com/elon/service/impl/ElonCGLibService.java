package com.elon.service.impl;

import com.elon.core.proxy.anotation.CGLibProxy;

/**
 * 2017/2/23 10:42.
 * <p>
 * Email: cheerUpPing@163.com
 */
@CGLibProxy(callBackVal = 1)
public class ElonCGLibService {


    public void elonCglib(String param) {
        System.out.println("ElonCGLibService服务................" + param);
    }

}
