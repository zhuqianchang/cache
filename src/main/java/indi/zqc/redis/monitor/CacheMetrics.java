package indi.zqc.redis.monitor;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import indi.zqc.redis.service.impl.CacheFacadeImpl;

/**
 * Title : CacheMetrics.java
 * Package : indi.zqc.redis.monitor
 * Description : 缓存服务质量
 * Create on : 2018/1/17 14:53
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public class CacheMetrics implements Metrics {

    private MetricRegistry metricRegistry;

    //set操作执行情况
    private Timer setOperationTimer;

    //get操作执行情况
    private Timer getOperationTimer;

    //缓存命中次数
    private Counter getHitCounter;

    //缓存未命中次数
    private Counter getMissCounter;

    //缓存命中率
    private Gauge<Double> getHitRate;

    public CacheMetrics() {
        this.metricRegistry = new MetricRegistry();
        this.setOperationTimer = metricRegistry.timer(MetricRegistry.name(CacheFacadeImpl.class, "setCache"));
        this.getOperationTimer = metricRegistry.timer(MetricRegistry.name(CacheFacadeImpl.class, "getCache"));
        this.getHitCounter = metricRegistry.counter(MetricRegistry.name(CacheFacadeImpl.class, "getHit"));
        this.getMissCounter = metricRegistry.counter(MetricRegistry.name(CacheFacadeImpl.class, "getMiss"));
        this.getHitRate = metricRegistry.register(MetricRegistry.name(CacheFacadeImpl.class, "getHitRate"), new Gauge<Double>() {
            public Double getValue() {
                if (getHitCounter.getCount() == 0 && getMissCounter.getCount() == 0) return 1.0d;
                return (double) getHitCounter.getCount() / (double) (getHitCounter.getCount() + getMissCounter.getCount());
            }
        });
    }

    public Timer.Context createSetOperationContext() {
        return this.setOperationTimer.time();
    }

    public Timer.Context createGetOperationContext() {
        return this.getOperationTimer.time();
    }

    public void increaseGetHit() {
        this.getHitCounter.inc();
    }

    public void increaseGetMiss() {
        this.getMissCounter.inc();
    }

    public MetricRegistry getMetricRegistry() {
        return this.metricRegistry;
    }

    public long getMissCount() {
        return this.getMissCounter.getCount();
    }

    public long getHitCount() {
        return this.getHitCounter.getCount();
    }

    public double getHitRate() {
        return this.getHitRate.getValue();
    }

    public Timer getSetOperationTimer() {
        return this.setOperationTimer;
    }

    public Timer getGetOperationTimer() {
        return this.getOperationTimer;
    }

    public long getGetOperationTimerCount() {
        return this.getOperationTimer.getCount();
    }

    public long getSetOperationTimerCount() {
        return this.setOperationTimer.getCount();
    }
}
