package indi.zqc.redis.configuration;

import indi.zqc.redis.handler.support.RedisCacheHandler;
import indi.zqc.redis.monitor.CacheMetrics;
import indi.zqc.redis.service.CacheFacade;
import indi.zqc.redis.service.impl.CacheFacadeImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;

/**
 * Title : RedisConfiguration.java
 * Package : indi.zqc.redis.configuration
 * Description : Redis配置
 * Create on : 2018/1/17 14:28
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.hostName:127.0.0.1}")
    private String hostName;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    @Value("${spring.redis.pool.maxActive:8}")
    private int maxActive;

    @Value("${spring.redis.pool.maxWait:-1}")
    private long maxWait;

    @Value("${spring.redis.pool.maxIdle:8}")
    private int maxIdle;

    @Value("${spring.redis.pool.minIdle:0}")
    private int minIdle;

    @Value("${spring.redis.timeout:2000}")
    private int timeout;

    @Value("${spring.redis.sentinel.nodes:}")
    private String sentinelNodes;

    @Value("${spring.redis.sentinel.master:master}")
    private String master;

    @Value("${spring.redis.cluster.nodes:}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.max-redirects:3}")
    private int maxRedirects;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = null;
        if (StringUtils.hasText(sentinelNodes)) {
            //sentinel
            ArrayList<RedisNode> redisNodes = parseRedisNodes(sentinelNodes);
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            redisSentinelConfiguration.setSentinels(redisNodes);
            redisSentinelConfiguration.setMaster(master);
            factory = new JedisConnectionFactory(redisSentinelConfiguration);
        }
        if (factory == null && StringUtils.hasText(clusterNodes)) {
            //cluster
            ArrayList<RedisNode> redisNodes = parseRedisNodes(clusterNodes);
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            redisClusterConfiguration.setClusterNodes(redisNodes);
            redisClusterConfiguration.setMaxRedirects(maxRedirects);
            factory = new JedisConnectionFactory(redisClusterConfiguration);
        }
        if (factory == null) {
            //非集群
            factory = new JedisConnectionFactory();
            factory.setHostName(hostName);
            factory.setPort(port);
        }
        factory.setPassword(password);
        factory.setDatabase(database);
        factory.setTimeout(timeout);
        factory.setPoolConfig(jedisPoolConfig());
        return factory;
    }


    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public CacheFacade<CacheMetrics> cache(RedisTemplate redisTemplate) {
        return new CacheFacadeImpl(new RedisCacheHandler(redisTemplate));
    }

    private ArrayList<RedisNode> parseRedisNodes(String nodes) {
        ArrayList<RedisNode> ret = new ArrayList<>();
        for (String node : nodes.split(",")) {
            if (node.split(":").length > 1) {
                ret.add(new RedisNode(node.split(":")[0], Integer.parseInt(node.split(":")[1])));
            }
        }
        return ret;
    }

}
