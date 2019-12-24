package com.lzq.selfdiscipline.business.util;

import com.lzq.selfdiscipline.business.properties.RedisProperties;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

public class RedisClusterSingleton {

    private RedisClusterSingleton() {
    }

    /**
     * 定义静态内部类：初始化 jedisCluster
     */
    private static class JedisClusterSingleton {
        private static final JedisCluster jedisCluster;

        static {
            JedisPoolConfig jedisPool = new JedisPoolConfig();
            // 最大连接数
            jedisPool.setMaxTotal(RedisProperties.maxTotal);
            // 最大空闲连接数
            jedisPool.setMaxIdle(RedisProperties.maxIdle);
            // 最小空闲连接数
            jedisPool.setMinIdle(RedisProperties.minIdle);
            // 最大等待时间
            jedisPool.setMaxWaitMillis(RedisProperties.maxWaitMillis);
            Set<HostAndPort> nodes = new HashSet<>();
            // 集群节点ip:port （多个以逗号隔开）
            String[] hostPortNodes = RedisProperties.hostPorts.split(",");
            String[] hostPort;
            for (String node : hostPortNodes) {
                hostPort = node.split(":");
                nodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
            }
            jedisCluster = new JedisCluster(nodes, jedisPool);
        }
    }

    /**
     * 获取redisCluster
     * @return
     */
    public static JedisCluster getJedisCluster() {
        return JedisClusterSingleton.jedisCluster;
    }
}
