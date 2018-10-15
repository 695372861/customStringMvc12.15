package com.elon.service.impl;

import com.elon.core.anotation.AutoWire;
import com.elon.core.anotation.Service;
import com.elon.dao.HarmonyDao;

/**
 * 2017/2/14 15:16.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Service(name = "harmonyService")
public class HarmonyService {

    @AutoWire
    private HarmonyDao harmonyDao;

    public String doGreen(String param) {
        return harmonyDao.doGreen(param);
    }

    public String doDinner(String param) {
        return harmonyDao.doDinner(param);
    }

}
