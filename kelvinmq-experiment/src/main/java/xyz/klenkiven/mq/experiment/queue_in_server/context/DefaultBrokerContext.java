package xyz.klenkiven.mq.experiment.queue_in_server.context;

import xyz.klenkiven.mq.model.Connection;

import java.util.UUID;

public class DefaultBrokerContext implements BrokerContext {

    @Override
    public Connection handleConnection(Connection connection) {
        connection.setConnectionId(UUID.randomUUID().toString());
        System.out.println("NEW_INCOME::Connection: Channel: " + connection.getChannel() +
                ", ConnectionId: " + connection.getConnectionId());
        return connection;
    }
}
