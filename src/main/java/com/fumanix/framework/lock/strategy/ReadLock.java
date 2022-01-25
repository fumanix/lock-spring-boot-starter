package com.fumanix.framework.lock.strategy;

import com.fumanix.framework.lock.domain.LockAction;
import org.redisson.api.RReadWriteLock;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 读锁
 */
@Component("read")
public class ReadLock extends Lock {

    /**
     * 获取锁
     * @return
     */
    @Override
    public boolean acquire(LockAction action) {
        try {
            RReadWriteLock lock = redissonClient.getReadWriteLock(action.getKey());
            return lock.readLock().tryLock(action.getWaitTime(), action.getLeaseTime(), TimeUnit.SECONDS);
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
        RReadWriteLock lock = redissonClient.getReadWriteLock(action.getKey());
        if (Objects.nonNull(lock) &&
                lock.readLock().isLocked() && lock.readLock().isHeldByCurrentThread()){
            lock.readLock().unlock();
        }
    }
}
