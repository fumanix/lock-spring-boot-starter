package com.fumanix.framework.lock.handler;

import com.fumanix.framework.lock.domain.LockAction;
import com.fumanix.framework.lock.strategy.Lock;
import org.aspectj.lang.JoinPoint;

/**
 * @create: 2022-01-08 17:35
 */
public interface FailureStrategy {

    void handle(LockAction lockModel, Lock lock, JoinPoint joinPoint);
}
