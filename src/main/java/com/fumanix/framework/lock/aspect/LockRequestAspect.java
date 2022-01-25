package com.fumanix.framework.lock.aspect;

import com.fumanix.framework.lock.annotation.DLock;
import com.fumanix.framework.lock.domain.LockAction;
import com.fumanix.framework.lock.handler.LockActProvider;
import com.fumanix.framework.lock.strategy.Lock;
import com.fumanix.framework.lock.strategy.LockFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @create: 2022-01-05 17:44
 */
@Aspect
@Component
@Order(0)
public class LockRequestAspect {

    @Resource
    private LockFactory lockFactory;
    @Resource
    private LockActProvider lockActProvider;

    private final Logger log = LoggerFactory.getLogger(LockRequestAspect.class);

    /**
     * 环绕
     * @param joinPoint
     * @param dLock
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(dLock)")
    public Object around(ProceedingJoinPoint joinPoint, DLock dLock) throws Throwable {
        LockAction action = lockActProvider.get(joinPoint, dLock);
        Lock lock = lockFactory.getLock(action.getType());
        boolean acquireLock = lock.acquire(action);
        if(BooleanUtils.negate(acquireLock)) {
            log.warn("[获取锁失败] [lock]:{}, [threadId]:{}", action.getKey(), Thread.currentThread().getId());
            dLock.lockFail().handle(action, lock, joinPoint);
        }
        return joinPoint.proceed();
    }


    /**
     * 方法执行后操作
     * @param joinPoint
     * @param dLock
     * @throws Throwable
     */
    @AfterReturning(value = "@annotation(dLock)")
    public void afterReturning(JoinPoint joinPoint, DLock dLock) {
        LockAction action = lockActProvider.get(joinPoint, dLock);
        lockFactory.getLock(action.getType()).release(action);
    }

    /**
     * 方法执行后异常 操作
     * @param joinPoint
     * @param dLock
     * @param ex
     * @throws Throwable
     */
    @AfterThrowing(value = "@annotation(dLock)", throwing = "ex")
    public void afterThrowing (JoinPoint joinPoint, DLock dLock, Throwable ex) throws Throwable {
        LockAction action = lockActProvider.get(joinPoint, dLock);
        lockFactory.getLock(action.getType()).release(action);
        throw ex;
    }
}
