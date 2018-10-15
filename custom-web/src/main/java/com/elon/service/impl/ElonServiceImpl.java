package com.elon.service.impl;

import com.elon.service.ElonOtherAopService;
import com.elon.service.ElonAopService;
import com.elon.core.proxy.anotation.JDKProxy;

/**
 * 2017/2/23 13:57.
 * <p>
 * Email: cheerUpPing@163.com
 */
@JDKProxy(callBackVal = 1)
public class ElonServiceImpl implements ElonAopService, ElonOtherAopService {


    public void driveSlow() {
        System.out.println("jdk代理,ElonService继承的方法");
    }

    public void driveFast() {
        System.out.println("jdk代理,ElonOtherService继承的方法");
    }
}
