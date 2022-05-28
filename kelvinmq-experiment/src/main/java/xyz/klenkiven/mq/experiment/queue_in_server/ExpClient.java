package xyz.klenkiven.mq.experiment.queue_in_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import xyz.klenkiven.mq.command.ConnectionCommand;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameDecoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameEncoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.HeartbeatHandler;
import xyz.klenkiven.mq.model.builder.FrameBuilder;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.UUID;

public class ExpClient {
    private static final int clientId = (int) UUID.randomUUID().getMostSignificantBits();

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LoggingHandler());

                        /* Frame Decoder and Encoder */
                        pipeline.addLast(new FrameDecoder());
                        pipeline.addLast(new FrameEncoder());

                        /* CONNECTION */
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(new ConnectionCommand(new Date()).toFrame());
                                ctx.fireChannelActive();
                            }
                        });

                        /* Heartbeat */
                        pipeline.addLast(new HeartbeatHandler(pipeline));

                    }
                }).connect(new InetSocketAddress("localhost", 5783)).sync().channel();

        // 获取关闭 Future
        ChannelFuture closeFuture = channel.closeFuture();
    }
}
