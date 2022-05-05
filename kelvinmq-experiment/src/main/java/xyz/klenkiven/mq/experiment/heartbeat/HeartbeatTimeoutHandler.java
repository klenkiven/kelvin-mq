package xyz.klenkiven.mq.experiment.heartbeat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HeartbeatTimeoutHandler extends ChannelInboundHandlerAdapter {

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
