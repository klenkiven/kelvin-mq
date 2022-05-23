package xyz.klenkiven.mq.experiment.queue_in_server.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import xyz.klenkiven.mq.model.builder.FrameBuilder;

/**
 * 心跳机制 -- 客户端实现
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    public static final int HEARTBEAT_FREQUENCY = 3;

    public HeartbeatHandler(ChannelPipeline pipeline) {
        pipeline.addFirst(
                new IdleStateHandler(
                        0,
                        HEARTBEAT_FREQUENCY,
                        0)
        );
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent stateEvent) {
            if (stateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("等待超时，发送心跳包");
                ctx.writeAndFlush(FrameBuilder.heartbeat().build());
            }
        }
    }
}
