package indi.zqc.redis.monitor;

/**
 * Title : MetricsService.java
 * Package : indi.zqc.redis.monitor
 * Description : 服务质量
 * Create on : 2018/1/17 14:42
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public interface MetricsService<T extends Metrics> {

    T getMetrics();

}
