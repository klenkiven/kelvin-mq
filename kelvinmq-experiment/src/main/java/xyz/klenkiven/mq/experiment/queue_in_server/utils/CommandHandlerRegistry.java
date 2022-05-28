package xyz.klenkiven.mq.experiment.queue_in_server.utils;

import io.netty.channel.ChannelPipeline;
import xyz.klenkiven.mq.experiment.queue_in_server.context.BrokerContext;
import xyz.klenkiven.mq.experiment.queue_in_server.handler.ConnectionCommandHandler;

public class CommandHandlerRegistry {
    public CommandHandlerRegistry(ChannelPipeline pipeline, BrokerContext brokerContext) {
        init(pipeline, brokerContext);
    }

    private void init(ChannelPipeline pipeline, BrokerContext brokerContext) {
        pipeline.addLast(new ConnectionCommandHandler(brokerContext));
    }
}
