package indi.zqc.redis.serialize;

/**
 * Title : SerializeException.java
 * Package : indi.zqc.redis.serialize
 * Description : 序列化异常
 * Create on : 2018/1/17 14:04
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
