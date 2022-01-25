package com.fumanix.framework.lock.config;

import com.fumanix.framework.lock.consts.LockType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @create: 2022-01-06 18:15
 */
@ConfigurationProperties(prefix = LockProperties.PREFIX)
public class LockProperties {

    public static final String PREFIX = "spring.lock";

    /**
     * 类型
     */
    private LockType type;

    /**
     * Redisson 配置
     */
    private final Redssion redisson = new Redssion();

    /**
     * Redssion 配置
     */
    public static class Redssion{

        /**
         * 模式
         */
        private Model model;

        /**
         * Netty线程池数量
         */
        private Integer nettyThreads = 32;

        /**
         * 单节点模式:节点地址
         */
        private String address;

        /**
         * 单节点模式:连接池大小
         */
        private Integer connectionPoolSize = 64;

        /**
         * 哨兵模式:主服务器的名称
         */
        private String masterName;

        /**
         * 多节点模式:节点地址
         */
        private List<String> nodeAddresses = new ArrayList<String>();

        /**
         * 主从模式:主节点地址
         */
        private String masterAddress;

        /**
         * 主从模式:从主节点地址
         */
        private List<String> slaveAddress = new ArrayList<String>();

        /**
         * 密码
         */
        private String password;

        /**
         * 数据库编号
         */
        private Integer database = 0;



        public enum Model {
            /**
             * 单例
             */
            SINGLE,

            /**
             * 哨兵
             */
            SENTINEL,

            /**
             * 集群
             */
            CLUSTER,

            /**
             * 主从
             */
            MASTERSLAVE,

            /**
             * 云托管模式
             */
            REPLICATED
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMasterName() {
            return masterName;
        }

        public void setMasterName(String masterName) {
            this.masterName = masterName;
        }

        public List<String> getNodeAddresses() {
            return nodeAddresses;
        }

        public void setNodeAddresses(List<String> nodeAddresses) {
            this.nodeAddresses = nodeAddresses;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getDatabase() {
            return database;
        }

        public void setDatabase(Integer database) {
            this.database = database;
        }

        public String getMasterAddress() {
            return masterAddress;
        }

        public void setMasterAddress(String masterAddress) {
            this.masterAddress = masterAddress;
        }

        public List<String> getSlaveAddress() {
            return slaveAddress;
        }

        public void setSlaveAddress(List<String> slaveAddress) {
            this.slaveAddress = slaveAddress;
        }

        public Integer getConnectionPoolSize() {
            return connectionPoolSize;
        }

        public void setConnectionPoolSize(Integer connectionPoolSize) {
            this.connectionPoolSize = connectionPoolSize;
        }

        public Integer getNettyThreads() {
            return nettyThreads;
        }

        public void setNettyThreads(Integer nettyThreads) {
            this.nettyThreads = nettyThreads;
        }

    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }

    public Redssion getRedisson() {
        return redisson;
    }
}
