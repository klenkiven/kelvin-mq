package xyz.klenkiven.mq.serializer;

public interface Serializer {

    /**
     * 对象序列化
     */
    byte[] serialize(Object obj);

    /**
     * 对象反序列化
     */
    <T> T deserialize(Class<T> type);
}
