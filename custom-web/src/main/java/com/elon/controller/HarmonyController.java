package com.elon.controller;

import com.elon.core.anotation.AutoWire;
import com.elon.core.anotation.Controller;
import com.elon.core.anotation.RequestMapping;
import com.elon.service.impl.HarmonyService;

import javax.servlet.http.HttpServletRequest;

/**
 * 2017/2/14 15:22.
 * <p>
 *
 */
@Controller(name = "harmonyController")
@RequestMapping(value = "/harmony")
public class HarmonyController {

    @AutoWire
    private HarmonyService harmonyService;

    @RequestMapping(value = "/doGreen")
    public String doGreen(String param, HttpServletRequest request) {
        String str=request.getParameter("param");
        if("".equals(str))
        {
            str="默认String";
        }
        return harmonyService.doGreen(str);
    }

    @RequestMapping(value = "/doDinner")
    public String doDinner(String param) {
        return harmonyService.doDinner(param);
    }

}
