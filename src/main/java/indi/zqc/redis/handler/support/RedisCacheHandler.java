package indi.zqc.redis.handler.support;

import indi.zqc.redis.handler.CacheHandler;
import indi.zqc.redis.serialize.ISerializer;
import indi.zqc.redis.serialize.support.JSONSerializer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * Title : RedisCacheHandler.java
 * Package : indi.zqc.redis.handler.support
 * Description : 缓存操作实现，基于redis
 * Create on : 2018/1/17 13:28
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public class RedisCacheHandler implements CacheHandler {

    private RedisTemplate redisTemplate;

    private ISerializer iSerializer;

    public RedisCacheHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.iSerializer = JSONSerializer.getDefaultInstance();
    }

    @Override
    public void set(String key, Object value) {
        set(key, value, 0L);
    }

    @Override
    public void set(final String key, final Object value, final long expire) {
        if (value == null) {
            delete(key);
            return;
        }
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = key.getBytes();
                connection.set(keyBytes, iSerializer.serialize(value));
                if (expire > 0) {
                    connection.expire(keyBytes, expire);
                }
                return 1L;
            }
        });
    }

    @Override
    public String get(String key) {
        return get(key, String.class);
    }

    @Override
    public <T> T get(final String key, final Class<T> tpl) {
        return (T) redisTemplate.execute(new RedisCallback() {
            @Override
            public T doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.get(key.getBytes());
                return iSerializer.deserialize(value, tpl);
            }
        });
    }

    @Override
    public String[] keys(final String pattern) {
        return (String[]) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Set<byte[]> keysSet = connection.keys(pattern.getBytes());
                String[] ret = new String[keysSet.size()];
                int index = 0;
                for (byte[] k : keysSet) {
                    ret[index++] = new String(k);
                }
                return ret;
            }
        });
    }

    @Override
    public boolean exists(final String key) {
        return (boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    @Override
    public void delete(String... keys) {
        for (final String key : keys) {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.del(key.getBytes());
                }
            });
        }
    }

    @Override
    public void expire(final String key, final long expire) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                if (expire > 0) {
                    connection.expire(key.getBytes(), expire);
                } else {
                    connection.del(key.getBytes());
                }
                return 1L;
            }
        });
    }
}
