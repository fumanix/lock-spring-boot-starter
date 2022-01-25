package com.fumanix.framework.lock.strategy;

import com.fumanix.framework.lock.domain.LockAction;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;

/**
 * @create: 2022-01-04 17:52
 */
public abstract class Lock {

    @Resource
    protected RedissonClient redissonClient;

    /**
     * 获取锁
     * @return
     */
    public abstract boolean acquire(LockAction action);

    /**
     * 释放锁
     * @return
     */
    public abstract void release(LockAction action);
}
