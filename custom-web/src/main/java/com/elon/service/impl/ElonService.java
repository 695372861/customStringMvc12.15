package com.elon.service.impl;

import com.elon.core.anotation.AutoWire;
import com.elon.core.anotation.Service;
import com.elon.dao.ElonDao;

/**
 * 2017/2/14 15:09.
 * <p>
 * Email: cheerUpPing@163.com
 */
@Service(name = "elonService")
public class ElonService {

    @AutoWire(name = "elonDao")
    private ElonDao elonDao;

    public String sayHello(String param) {
        return elonDao.sayHello(param);
    }

    public String sayGoodBye(String param) {
        return elonDao.sayGoodBye(param);
    }

}
