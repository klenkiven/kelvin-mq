package xyz.klenkiven.mq.experiment.queue_in_server.handler;

import io.netty.channel.SimpleChannelInboundHandler;
import xyz.klenkiven.mq.experiment.queue_in_server.context.BrokerContext;

public abstract class CommandHandler<T> extends SimpleChannelInboundHandler<T> {
    protected final BrokerContext context;

    /**
     * For Command Handler
     */
    public CommandHandler(BrokerContext context) {
        this.context = context;
    }

}
