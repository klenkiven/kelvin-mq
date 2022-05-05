package xyz.klenkiven.mq.prototype;

import java.util.Set;

/**
 * 客户端连接
 */
public interface Connection {

    void shutdown();

    void handleMessage(String topic, Message message);

    boolean subscribe(String topic);

    void unsubscribe(String topic);

    void publish(String topic, Message message);

    String getConnectionId();

    Set<String> subscribedTopicSet();

    boolean isRunning();
}
