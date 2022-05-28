package xyz.klenkiven.mq.experiment.queue_in_server.context;

import xyz.klenkiven.mq.model.Connection;

/**
 * 消息代理的上下文操作
 */
public interface BrokerContext {

    Connection handleConnection(Connection connection);

}
