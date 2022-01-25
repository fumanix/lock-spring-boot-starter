package com.fumanix.framework.lock.strategy;

import com.fumanix.framework.lock.consts.LockMode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2022-01-06 10:44
 */
@Component
public class LockFactory {

    @Resource
    private Map<String, Lock> lockStrategy = new HashMap<>();

    /**
     * 获取锁
     * @param type
     * @return
     */
    public Lock getLock(LockMode type) {
        return lockStrategy.get(type.code());
    }
}
