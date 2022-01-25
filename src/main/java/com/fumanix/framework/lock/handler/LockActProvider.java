package com.fumanix.framework.lock.handler;

import com.fumanix.framework.lock.annotation.DLock;
import com.fumanix.framework.lock.consts.LockMode;
import com.fumanix.framework.lock.domain.LockAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Component
public class LockActProvider {

    private static final String LOCK_NAME_PREFIX = "redis.lock:";

    @Autowired
    private LockKeyProvider lockKeyProvider;

    /**
     * 获取锁信息
     * @param joinPoint
     * @param lock
     * @return
     */
    public LockAction get(JoinPoint joinPoint, DLock lock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockMode type= lock.lockType();
        String businessKeyName=lockKeyProvider.getKeyName(joinPoint,lock);
        String name = StringUtils.hasText(lock.name()) ? lock.name()
                : MessageFormat.format("{0}.{1}", signature.getDeclaringTypeName(), signature.getMethod().getName());
        String lockKey = MessageFormat.format("{0}.{1}.{2}",LOCK_NAME_PREFIX, name, businessKeyName);
        long waitTime = lock.waitTime()==Long.MIN_VALUE ? 60 : lock.waitTime();
        long leaseTime = lock.leaseTime()==Long.MIN_VALUE ? 60 : lock.leaseTime();
        return new LockAction(type,lockKey,waitTime,leaseTime);
    }
}
