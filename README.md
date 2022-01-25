# Lock
 基于redis的分布式锁spring-boot starter组件

# 构建上传
> 修改 gradle.properties  版本。执行以下命令上传到maven私有仓库
```
version=0.0.1
```
- 上传文件
```
# gradle publish 
```

# 快速开始
- 添加依赖

``` 
# maven
<dependency>
  <groupId>com.fumanix.framework</groupId>
  <artifactId>lock-spring-boot-starter</artifactId>
  <version>0.0.1</version>
</dependency>
```

``` 
# gradle
implementation 'com.fumanix.framework:lock-spring-boot-starter:0.0.1'
```

- 使用案例

> 项目配置

``` bash
spring:
  lock:
    type: redisson
    redisson:
      model: single
      address: 127.0.0.1:6379
      password: *******
      database: 15
      connection-pool-size: 128
    connect-timeout: 1000
```
> 业务实现

``` java
    @DLock(name = "business", keys = {"#data"}, waitTime = 3, leaseTime = 4, lockFail = LockFailHandler.FAIL_FAST)
    public String handle(String data) throws Exception {
        log.info(" 开始：{}, {}", data, System.currentTimeMillis());
        Thread.sleep(1000*3);
        log.info(" 结束：{}, {}", data, System.currentTimeMillis());
        return "success";
    }
```

> 注解说明

- `name`：lock的name，对应redis的key值。默认为：类名+方法名
- `lockType`：锁的类型，目前支持（可重入锁，公平锁，读写锁）。默认为：可重入锁
- `waitTime`：获取锁最长等待时间。默认为：60s。
- `leaseTime`：获得锁后，自动释放锁的时间。根据业务可适当设置大。默认为：60s。
- `lockFail`: 加锁失败的处理策略，可配置为不做处理、快速失败、阻塞等待的处理策略，默认策略为快速失败

> 加锁失败处理策略(LockFailHandler)：

- `NO_OPERATION` 不做处理，继续执行业务逻辑
- `FAIL_FAST` 快速失败，会抛出IllegalArgumentException
- `KEEP_ACQUIRE` 阻塞等待，一直阻塞，直到获得锁，但在太多的尝试后，会停止获取锁并报错，此时很有可能是发生了死锁。

> 参考说明
- [Redisson Wiki](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95) 