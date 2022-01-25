package com.fumanix.framework.lock.consts;

/**
 * 锁类型
 * @create: 2022-01-05 15:57
 */
public enum LockMode {

    /**
     * 可重入锁
     */
    Reentrant("reentrant"),

    /**
     * 公平锁
     */
    Fair("fair"),

    /**
     * 读锁
     */
    Read("read"),

    /**
     * 写锁
     */
    Write("write");

    private String code;

    public String code() {
        return code;
    }

    LockMode(String code) {
        this.code = code;
    }

}
