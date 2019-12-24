package com.lzq.selfdiscipline.business.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * redis配置文件类
 */
@Component
public class RedisProperties {
    // 集群节点ip:port（多个以逗号隔开）
    public static String hostPorts;
    // 密码
    public static String password;
    // 最大连接数
    public static Integer maxTotal;
    // 最大空闲连接数
    public static Integer maxIdle;
    // 最小空闲连接数
    public static Integer minIdle;
    // 最大等待时间
    public static Integer maxWaitMillis;

    // 集群节点ip:port（多个以逗号隔开）
    @Value("${redis.host-ports}")
    private String host_ports;
    // 密码
    @Value("${redis.password}")
    private String pwd;
    // 最大连接数
    @Value("${redis.jedis-pool-config.max-total}")
    private Integer max_total;
    // 最大空闲连接数
    @Value("${redis.jedis-pool-config.max-idle}")
    private Integer max_idle;
    // 最小空闲连接数
    @Value("${redis.jedis-pool-config.min-idle}")
    private Integer min_idle;
    // 最大等待时间
    @Value("${redis.jedis-pool-config.max-wait-millis}")
    private Integer max_wait_millis;

    @PostConstruct
    public void init() {
        Objects.requireNonNull(this.host_ports, "获取redis集群节点ip:port失败!!!");
        Objects.requireNonNull(this.pwd, "获取redis密码失败!!!");
        Objects.requireNonNull(this.max_total, "获取redis最大连接数失败!!!");
        Objects.requireNonNull(this.max_idle, "获取redis最大空闲连接数失败!!!");
        Objects.requireNonNull(this.min_idle, "获取redis最小空闲连接数失败!!!");
        Objects.requireNonNull(this.max_wait_millis, "获取redis最大等待时间失败!!!");
        hostPorts = this.host_ports;
        password = this.pwd;
        maxTotal = this.max_total;
        maxIdle = this.max_idle;
        minIdle = this.min_idle;
        maxWaitMillis = this.max_wait_millis;
    }
}
