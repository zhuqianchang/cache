package indi.zqc.redis.service.impl;

import com.codahale.metrics.Timer;
import indi.zqc.redis.handler.CacheHandler;
import indi.zqc.redis.monitor.CacheMetrics;
import indi.zqc.redis.service.CacheFacade;

/**
 * Title : CacheFacheImpl.java
 * Package : indi.zqc.redis.service.impl
 * Description : 缓存服务实现
 * Create on : 2018/1/17 14:45
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public class CacheFacadeImpl<T extends CacheMetrics> implements CacheFacade<T> {

    private CacheHandler cacheHandler;

    private CacheMetrics metrics;

    public CacheFacadeImpl(CacheHandler cacheHandler) {
        this.cacheHandler = cacheHandler;
        this.metrics = new CacheMetrics();
    }

    @Override
    public void set(String key, Object value) {
        Timer.Context ctx = metrics.createSetOperationContext();
        cacheHandler.set(key, value);
        ctx.stop();
    }

    @Override
    public void set(String key, Object value, long expire) {
        Timer.Context ctx = metrics.createSetOperationContext();
        cacheHandler.set(key, value, expire);
        ctx.stop();
    }

    @Override
    public String get(String key) {
        Timer.Context ctx = metrics.createGetOperationContext();
        String value = cacheHandler.get(key);
        ctx.stop();
        if (value == null) {
            metrics.increaseGetMiss();
        } else {
            metrics.increaseGetHit();
        }
        return value;
    }

    @Override
    public <T> T get(String key, Class<T> tpl) {
        Timer.Context ctx = metrics.createGetOperationContext();
        T value = cacheHandler.get(key, tpl);
        ctx.stop();
        if (value == null) {
            metrics.increaseGetMiss();
        } else {
            metrics.increaseGetHit();
        }
        return value;
    }

    @Override
    public String[] keys(String pattern) {
        return cacheHandler.keys(pattern);
    }

    @Override
    public boolean exists(String key) {
        return cacheHandler.exists(key);
    }

    @Override
    public void delete(String... keys) {
        cacheHandler.delete(keys);
    }

    @Override
    public void expire(String key, long expire) {
        cacheHandler.expire(key, expire);
    }

    @Override
    public T getMetrics() {
        return (T) metrics;
    }
}
