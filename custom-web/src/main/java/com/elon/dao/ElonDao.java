package com.elon.dao;

import com.elon.core.anotation.Service;
import com.elon.core.util.Log;

/**
 * 2017/2/14 15:10.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Service(name = "elonDao")
public class ElonDao {


    public String sayHello(String param) {
        Log.info("你好," + param);
        return param;
    }

    public String sayGoodBye(String param) {
        Log.info("再见," + param);
        return param;
    }

}
