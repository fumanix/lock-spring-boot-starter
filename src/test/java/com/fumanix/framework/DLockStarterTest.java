package com.fumanix.framework;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
public class DLockStarterTest {

    @Resource
    private DLockTestService dLockTestService;

    private final Logger log = LoggerFactory.getLogger(DLockStarterTest.class);


    /**
     * 同一进程多线程获取锁
     * @throws Exception
     */
    @Test
    public void multithreading()throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        IntStream.range(0,10).forEach(i-> executorService.submit(() -> {
            try {
                String result = dLockTestService.handle("process");
                log.info(" 线程[{}], 结果[{}]", Thread.currentThread().getName(), result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }


    /**
     * 加锁超时快速失败
     */
    @Test
    public void failFast() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(() -> {
            try {
                dLockTestService.timeout1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        TimeUnit.MILLISECONDS.sleep(1000*10);
    }


    /**
     * 测试加锁超时阻塞等待
     * 会打印10次acquire lock
     */
    @Test
    public void lockTimeoutKeepAcquire() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(10);
        for(int i=0; i<10; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    dLockTestService.timeout2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        long start = System.currentTimeMillis();
        startLatch.countDown();
        endLatch.await();
        long end = System.currentTimeMillis();
        assert (end - start) >= 10*2*1000;
    }


    /**
     * 测试加锁超时不做处理
     */
    @Test
    public void lockTimeoutNoOperation() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(10);
        for(int i=0; i<10; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    dLockTestService.timeout3();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });

        }
        long start = System.currentTimeMillis();
        startLatch.countDown();
        endLatch.await();
        long end = System.currentTimeMillis();
        assert (end - start) < 10*2*1000;
    }
}
