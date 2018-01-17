package indi.zqc.redis.serialize;

/**
 * Title : ISerializer.java
 * Package : indi.zqc.redis.serialize
 * Description : 序列换接口
 * Create on : 2018/1/17 13:30
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public interface ISerializer {

    byte[] serialize(Object o);

    <T> T deserialize(byte[] data, Class<T> tpl);
}
