package com.fumanix.framework.lock.annotation;

import com.fumanix.framework.lock.consts.LockMode;
import com.fumanix.framework.lock.handler.LockFailHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 * @create: 2022-01-25 14:00
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DLock {

    /**
     * 锁的名称
     * @return name
     */
    String name() default "";

    /**
     * 锁类型，默认可重入锁
     * @return lockType
     */
    LockMode lockType() default LockMode.Reentrant;

    /**
     * 尝试加锁，最多等待时间
     * @return waitTime
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     *上锁以后xxx秒自动解锁
     * @return leaseTime
     */
    long leaseTime() default Long.MIN_VALUE;

    /**
     * 自定义业务key
     * @return keys
     */
    String [] keys() default {};

    /**
     * 加锁超时的处理策略 快速失败  Locking failure
     * @return lockTimeoutStrategy
     */
    LockFailHandler lockFail() default LockFailHandler.FAIL_FAST;
}
