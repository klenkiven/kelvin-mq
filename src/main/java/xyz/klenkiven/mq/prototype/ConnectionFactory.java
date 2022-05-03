package xyz.klenkiven.mq.prototype;

import xyz.klenkiven.mq.prototype.impl.ConnectionImpl;

import java.util.UUID;

/**
 * 客户端连接工厂
 */
public abstract class ConnectionFactory {

    /**
     * 新建一个连接
     */
    public static Connection newConnection(String connectionId, Broker broker) {
        return new ConnectionImpl(connectionId, broker);
    }

    /**
     * 新建一个连接
     */
    public static Connection newConnection(Broker broker) {
        return newConnection(UUID.randomUUID().toString(), broker);
    }
}
