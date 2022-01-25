package com.fumanix.framework.lock.strategy;

import com.fumanix.framework.lock.domain.LockAction;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 可重入锁
 * @create: 2022-01-05 17:02
 */
@Component("reentrant")
public class ReentrantLock extends Lock {

    private final Logger log = LoggerFactory.getLogger(ReentrantLock.class);

    /**
     * 获取
     * @param action
     * @return
     */
    @Override
    public boolean acquire(LockAction action) {
        try {
            RLock lock = redissonClient.getLock(action.getKey());
            log.info("获取锁 [lock]:{}, [threadId]:{}", lock.getName(), Thread.currentThread().getId());
            return lock.tryLock(action.getWaitTime(), action.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }


    /**
     * 释放
     * @param action
     * @return
     */
    @Override
    public void release(LockAction action) {
        RLock lock = redissonClient.getLock(action.getKey());
        log.info("释放锁 [lock]:{}, [isHeld]:{}, [threadId]:{}", lock.getName(), lock.isHeldByCurrentThread(), Thread.currentThread().getId());
        if(Objects.nonNull(lock) && lock.isLocked() && lock.isHeldByCurrentThread()){
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("unLock happen err Found Lock({}) already been released, [threadId]:{}", lock.getName(), Thread.currentThread().getId());
            }
        }
    }
}
