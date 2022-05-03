package xyz.klenkiven.mq.prototype;

import java.util.Collection;

public interface Broker {

    void shutdown();

    Connection createConnection();

    Connection createConnection(String connectionId);

    boolean deleteConnection(String connectionId);

    void handlePublish(String topic, Message message);

    boolean handleSubscribe(String connectionId, String topic);

    void handleUnsubscribe(String connectionId, String topic);

    Collection<Connection> listConnectionId();

    Connection getConnection(String connectionId);
}
