package xyz.klenkiven.mq.experiment.queue_in_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameDecoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameEncoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.HeartbeatTimeoutHandler;

@Slf4j
public class ExpServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup child = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(boss, child)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LoggingHandler());

                        /* Frame Decoder and Encoder */
                        pipeline.addLast(new FrameDecoder());
                        pipeline.addLast(new FrameEncoder());

                        /* Heartbeat */
                        pipeline.addLast(new HeartbeatTimeoutHandler(pipeline));

                        /* Message Handler */

                        /* RPC Handler */
                    }
                }).bind(5783);
    }
}
