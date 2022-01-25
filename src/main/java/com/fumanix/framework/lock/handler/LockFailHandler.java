package com.fumanix.framework.lock.handler;

import com.fumanix.framework.lock.domain.LockAction;
import com.fumanix.framework.lock.strategy.Lock;
import org.aspectj.lang.JoinPoint;

import java.util.concurrent.TimeUnit;

/**
 * 获取锁超时的处理逻辑接口
 **/
public enum  LockFailHandler implements FailureStrategy{

    /**
     * 继续执行业务逻辑，不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockAction lockModel, Lock lock, JoinPoint joinPoint) {
            // do nothing
        }
    },

    /**
     * 快速失败
     */
    FAIL_FAST() {
        @Override
        public void handle(LockAction action, Lock lock, JoinPoint joinPoint) {
            throw new IllegalArgumentException("系统处理中,请稍后重试...");
        }
    },

    /**
     * 一直阻塞，直到获得锁，在太多的尝试后，仍会报错
     */
    KEEP_ACQUIRE() {
        private static final long DEFAULT_INTERVAL = 100L;
        private static final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000L;

        @Override
        public void handle(LockAction action, Lock lock, JoinPoint joinPoint) {
            long interval = DEFAULT_INTERVAL;
            while(!lock.acquire(action)) {
                if(interval > DEFAULT_MAX_INTERVAL) {
                    throw new IllegalArgumentException("系统处理中,请稍后重试...");
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(interval);
                    interval <<= 1;
                } catch (InterruptedException e) {
                    throw new IllegalArgumentException("Failed to acquire Lock", e);
                }
            }
        }
    }
}
