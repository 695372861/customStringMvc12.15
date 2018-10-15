package com.elon.dao;

import com.elon.core.anotation.Service;
import com.elon.core.util.Log;

/**
 * 2017/2/14 15:17.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Service(name = "harmonyDao")
public class HarmonyDao {

    public String doGreen(String param) {
        Log.info("炒菜:" + param);
        return param;
    }

    public String doDinner(String param) {
        Log.info("做饭:" + param);
        return param;
    }
}
