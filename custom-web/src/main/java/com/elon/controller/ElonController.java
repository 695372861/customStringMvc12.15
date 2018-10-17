package com.elon.controller;

import com.elon.service.ElonAopService;
import com.elon.service.impl.ElonCGLibService;
import com.elon.service.ElonOtherAopService;
import com.elon.core.anotation.AutoWire;
import com.elon.core.anotation.Controller;
import com.elon.core.anotation.RequestMapping;
import com.elon.service.impl.ElonService;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>
 *
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

    //在controller的设置中，现在只支持基础数据类型和HttpServletRequest和httpServletResponse的设置
    //如果设置其他的类型着取到的对象为null
    //如果要取大量数据可以在HttpServletRequest中进行设置
    @RequestMapping(value = "/sayHello")
    public String sayHello(String param, HttpServletRequest request,int flag) {
        String param1=request.getParameter("param");
        System.out.println("param1="+param1+" flag="+flag);
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
