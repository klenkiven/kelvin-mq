package xyz.klenkiven.mq.prototype.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.klenkiven.mq.prototype.Broker;
import xyz.klenkiven.mq.prototype.Connection;
import xyz.klenkiven.mq.prototype.ConnectionFactory;
import xyz.klenkiven.mq.prototype.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BrokerImpl implements Broker {

    public final Map<String, Connection> connections = new ConcurrentHashMap<>();

    public final Map<String, Set<String>> topicAndConnections = new ConcurrentHashMap<>();

    @Override
    public void shutdown() {
        // 关闭客户端连接
        for (Connection connection : connections.values()) {
            connection.shutdown();
        }

        log.debug("中间人关闭");
    }

    @Override
    public Connection createConnection() {
        String connectionId = UUID.randomUUID().toString();
        Connection connection = doCreate(connectionId);
        if (connection == null) return null;
        connections.put(connectionId, connection);
        return connection;
    }

    @Override
    public Connection createConnection(String connectionId) {
        Connection connection = doCreate(connectionId);
        if (connection == null) return null;
        connections.put(connectionId, connection);
        return connection;
    }

    private Connection doCreate(String connectionId) {
        Connection connection = null;
        if (!connections.containsKey(connectionId)) {
            connection = ConnectionFactory.newConnection(connectionId, this);
        }
        return connection;
    }

    @Override
    public boolean deleteConnection(String connectionId) {
        Connection connection = connections.remove(connectionId);
        if (connection == null) return false;
        for (String topic : connection.subscribedTopicSet()) {
            handleUnsubscribe(connectionId, topic);
        }
        connection.shutdown();
        return true;
    }

    @Override
    public void handlePublish(String topic, Message message) {
        if (!topicAndConnections.containsKey(topic)) {
            log.error("Topic: {}, 没有订阅者", topic);
            return;
        }
        for (String connectionId : topicAndConnections.get(topic)) {
            Connection connection = connections.get(connectionId);
            connection.handleMessage(topic, message);
        }

    }

    @Override
    public boolean handleSubscribe(String connectionId, String topic) {
        if (!topicAndConnections.containsKey(topic)) {
            Set<String> connectionIdSet = Collections.synchronizedSet(new HashSet<>());
            topicAndConnections.put(topic, connectionIdSet);
        }
        Set<String> connectionIdSet = topicAndConnections.get(topic);
        if (connectionIdSet.contains(connectionId)) return false;
        connectionIdSet.add(connectionId);
        return true;
    }

    @Override
    public void handleUnsubscribe(String connectionId, String topic) {
        Set<String> connectionIdSet = topicAndConnections.get(topic);
        connectionIdSet.remove(connectionId);
    }

    @Override
    public Collection<Connection> listConnectionId() {
        return connections.values();
    }

    @Override
    public Connection getConnection(String connectionId) {
        return connections.get(connectionId);
    }
}
