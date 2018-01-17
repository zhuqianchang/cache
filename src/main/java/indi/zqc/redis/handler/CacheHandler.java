package indi.zqc.redis.handler;

/**
 * Title : cacheHandler.java
 * Package : indi.zqc.redis.handler
 * Description : 缓存操作接口
 * Create on : 2018/1/17 13:24
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public interface CacheHandler {

    void set(String key, Object value);

    void set(String key, Object value, long expire);

    String get(String key);

    <T> T get(String key, Class<T> tpl);

    String[] keys(String pattern);

    boolean exists(String key);

    void delete(String... keys);

    void expire(String key, long expire);
}
