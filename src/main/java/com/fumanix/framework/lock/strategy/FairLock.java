package com.fumanix.framework.lock.strategy;

import com.fumanix.framework.lock.domain.LockAction;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 */
@Component("fair")
public class FairLock extends Lock {


    /**
     * 获取锁
     * @return
     */
    @Override
    public boolean acquire(LockAction action) {
        try {
            RLock rLock=redissonClient.getFairLock(action.getKey());
            return rLock.tryLock(action.getWaitTime(), action.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     * @return
     */
    @Override
    public void release(LockAction action) {
        RLock rLock=redissonClient.getFairLock(action.getKey());
        if (Objects.nonNull(rLock) &&
                rLock.isLocked() && rLock.isHeldByCurrentThread()){
            rLock.unlock();
        }
    }
}
