package com.fumanix.framework;

import com.fumanix.framework.lock.annotation.DLock;
import com.fumanix.framework.lock.handler.LockFailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @create: 2022-01-06 16:35
 */
@Component
public class DLockTestService {

    private final Logger log = LoggerFactory.getLogger(DLockStarterTest.class);

    @DLock(name = "business", keys = {"#data"}, waitTime = 3, leaseTime = 4, lockFail = LockFailHandler.FAIL_FAST)
    public String handle(String data) throws Exception {
        log.info(" 开始：{}, {}", data, System.currentTimeMillis());
        Thread.sleep(1000*3);
        log.info(" 结束：{}, {}", data, System.currentTimeMillis());
        return "success";
    }

    /**
     * 加锁超时快速失败
     * @throws Exception
     */
    @DLock(name = "timeout_fail-fast", waitTime = 3, leaseTime = 10, lockFail = LockFailHandler.FAIL_FAST)
    public void timeout1() throws InterruptedException{
        TimeUnit.SECONDS.sleep(3);
        log.info("lock fail fast [threadId] : {}", Thread.currentThread().getId());
    }

    /**
     * 超时阻塞等待
     * @throws InterruptedException
     */
    @DLock(name = "timeout_keep-acquire", waitTime = 3, leaseTime = 10, lockFail = LockFailHandler.KEEP_ACQUIRE)
    public void timeout2() throws InterruptedException{
        TimeUnit.SECONDS.sleep(3);
        log.info("lock keep acquire [threadId] : {}", Thread.currentThread().getId());
    }

    /**
     * 超时不做处理
     * @throws InterruptedException
     */
    @DLock(name = "timeout_no-operation", waitTime = 3, leaseTime = 10, lockFail = LockFailHandler.NO_OPERATION)
    public void timeout3() throws InterruptedException{
        TimeUnit.SECONDS.sleep(3);
        log.info("lock no operation [threadId] : {}", Thread.currentThread().getId());
    }

}
