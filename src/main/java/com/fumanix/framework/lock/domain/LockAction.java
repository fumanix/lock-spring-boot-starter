package com.fumanix.framework.lock.domain;

import com.fumanix.framework.lock.consts.LockMode;

/**
 * @create: 2022-01-06 09:27
 */
public class LockAction {

    private LockMode type;

    private String key;

    private long waitTime;

    private long leaseTime;

    public LockAction(LockMode type, String key, long waitTime, long leaseTime) {
        this.type = type;
        this.key = key;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public static LockAction of(LockMode type, String key, long waitTime, long leaseTime){
        return new LockAction(type, key, waitTime, leaseTime);
    }

    public LockMode getType() {
        return type;
    }

    public void setType(LockMode type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    @Override
    public String toString() {
        return "LockAction{" +
                "type=" + type +
                ", key='" + key + '\'' +
                ", waitTime=" + waitTime +
                ", leaseTime=" + leaseTime +
                '}';
    }
}
