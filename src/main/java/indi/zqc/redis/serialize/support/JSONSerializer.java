package indi.zqc.redis.serialize.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import indi.zqc.redis.serialize.ISerializer;
import indi.zqc.redis.serialize.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Title : JSONSerializer.java
 * Package : indi.zqc.redis.serialize
 * Description : JSON方式序列化
 * Create on : 2018/1/17 13:32
 *
 * @author Zhu.Qianchang
 * @version v1.0.0
 */
public class JSONSerializer implements ISerializer {

    private static final Logger logger = LoggerFactory.getLogger(JSONSerializer.class);

    private static final JSONSerializer DEFAULT_INSTANCE = new JSONSerializer();

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        objectMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private String charset = "utf-8";

    public static final JSONSerializer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public byte[] serialize(Object o) {
        if (o == null) {
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer;
        try {
            writer = new OutputStreamWriter(baos, charset);
            objectMapper.writeValue(writer, o);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
        byte[] data = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) {
        if (data == null || data.length == 0) {
            return null;
        }
        String s;
        try {
            s = new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        Object object;
        try {
            object = objectMapper.readValue(s, TypeFactory.rawClass(tpl));
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
        return (T) object;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
