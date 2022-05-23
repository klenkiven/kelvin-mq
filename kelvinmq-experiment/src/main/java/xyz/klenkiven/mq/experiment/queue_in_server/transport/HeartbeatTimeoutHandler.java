package xyz.klenkiven.mq.experiment.queue_in_server.transport;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class HeartbeatTimeoutHandler extends ChannelInboundHandlerAdapter {

    /**
     * Add Idle Handler By Heartbeat Handler
     */
    public HeartbeatTimeoutHandler(ChannelPipeline pipeline) {
        // 显示当前连接状态的 Handler
        // 如果发生超时事件就会发出 超时事件
        pipeline.addFirst(new IdleStateHandler(
                5,
                0,
                0,
                TimeUnit.SECONDS));
    }

    /**
     * 处理 IdleStateEvent
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            String stateType = null;
            switch (idleStateEvent.state()) {
                case READER_IDLE -> stateType = "READER_IDLE";
                case WRITER_IDLE -> stateType = "WRITER_IDLE";
                case ALL_IDLE -> stateType = "ALL_IDLE";
            }

            // 发生任何超时事件，关闭通道
            Channel channel = ctx.channel();
            channel.close().sync();
            log.debug("客户端{}超时，超时类型：{}", channel.remoteAddress(), stateType);
            log.info("客户端{} - 心跳超时", channel.remoteAddress());
        }
    }

}
