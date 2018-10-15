package com.elon.controller;

import com.elon.service.ElonAopService;
import com.elon.service.impl.ElonCGLibService;
import com.elon.service.ElonOtherAopService;
import com.elon.core.anotation.AutoWire;
import com.elon.core.anotation.Controller;
import com.elon.core.anotation.RequestMapping;
import com.elon.service.impl.ElonService;

/**
 * 2017/2/14 15:22.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Controller(name = "elonController")
@RequestMapping(value = "/elon")
public class ElonController {

    @AutoWire
    private ElonService elonService;

    @AutoWire
    private ElonCGLibService elonCGLibService;

    @AutoWire
    private ElonAopService elonAopService;//jdk动态代理

    @AutoWire
    private ElonOtherAopService elonOtherAopService;//jdk动态代理

    @RequestMapping(value = "/sayHello")
    public String sayHello(String param) {
        elonCGLibService.elonCglib(param);
        elonAopService.driveSlow();
        elonOtherAopService.driveFast();
        return elonService.sayHello(param);
    }

    @RequestMapping(value = "/sayGoodBye")
    public String sayGoodBye(String param) {
        return elonService.sayGoodBye(param);
    }


}
