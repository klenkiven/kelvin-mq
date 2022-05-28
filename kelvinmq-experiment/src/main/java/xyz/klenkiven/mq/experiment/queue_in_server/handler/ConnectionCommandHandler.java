package xyz.klenkiven.mq.experiment.queue_in_server.handler;

import io.netty.channel.ChannelHandlerContext;
import xyz.klenkiven.mq.command.ConnectionCommand;
import xyz.klenkiven.mq.experiment.queue_in_server.context.BrokerContext;
import xyz.klenkiven.mq.model.Connection;

public class ConnectionCommandHandler extends CommandHandler<ConnectionCommand> {

    public ConnectionCommandHandler(BrokerContext context) {
        super(context);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConnectionCommand msg) throws Exception {
        Connection connection = new Connection();
        connection.setCreateTime(msg.getTimestamp());
        connection.setChannel(ctx.channel());
        connection = context.handleConnection(connection);

        // 返回成功信息
         ctx.writeAndFlush(new ConnectionCommand.Ok(connection.getConnectionId()).toFrame());
    }
}
