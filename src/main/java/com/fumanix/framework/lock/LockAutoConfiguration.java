package com.fumanix.framework.lock;

import com.fumanix.framework.lock.config.LockProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({LockProperties.class})
public class LockAutoConfiguration {

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";

    @Autowired
    private LockProperties lockProperties;

    /**
     * 初始化 RedissonClient
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisson() {
        Config config = new Config();
        config.setNettyThreads(lockProperties.getRedisson().getNettyThreads());
        LockProperties.Redssion.Model model = lockProperties.getRedisson().getModel();
        if(Objects.isNull(model)){
            throw new IllegalArgumentException("请配置redisson节点模式(model)");
        }
        if (LockProperties.Redssion.Model.SINGLE==model) {
            config.useSingleServer()
                    .setAddress(REDIS_PROTOCOL_PREFIX + lockProperties.getRedisson().getAddress())
                    .setPassword(lockProperties.getRedisson().getPassword())
                    .setConnectionPoolSize(lockProperties.getRedisson().getConnectionPoolSize())
                    .setDatabase(lockProperties.getRedisson().getDatabase());
        } else if (LockProperties.Redssion.Model.CLUSTER==model) {
            String[] nodes = (lockProperties.getRedisson().getNodeAddresses())
                    .stream().map(addr->REDIS_PROTOCOL_PREFIX+addr).toArray(String[]::new);
            config.useClusterServers()
                    .addNodeAddress(nodes)
                    .setPassword(lockProperties.getRedisson().getPassword());
        } else if(LockProperties.Redssion.Model.CLUSTER==model){
            String[] nodes = (lockProperties.getRedisson().getNodeAddresses())
                    .stream().map(addr->REDIS_PROTOCOL_PREFIX+addr).toArray(String[]::new);
            config.useSentinelServers()
                    .setMasterName(lockProperties.getRedisson().getMasterName())
                    .addSentinelAddress(nodes)
                    .setPassword(lockProperties.getRedisson().getPassword())
                    .setDatabase(lockProperties.getRedisson().getDatabase());
        }
        return Redisson.create(config);
    }
}
