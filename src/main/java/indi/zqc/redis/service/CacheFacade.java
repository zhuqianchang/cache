package indi.zqc.redis.service;

import indi.zqc.redis.handler.CacheHandler;
import indi.zqc.redis.monitor.CacheMetrics;
import indi.zqc.redis.monitor.MetricsService;

/**
 * Title : CacheFache.java
 * Package : indi.zqc.redis.service
 * Description : 缓存接口
 * Create on : 2018/1/17 14:39
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public interface CacheFacade<T extends CacheMetrics> extends CacheHandler, MetricsService<T> {
}
