package xyz.klenkiven.mq.experiment.queue_in_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameDecoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.FrameEncoder;
import xyz.klenkiven.mq.experiment.queue_in_server.transport.HeartbeatHandler;

import java.net.InetSocketAddress;
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

                        /* Heartbeat */
                        pipeline.addLast(new HeartbeatHandler(pipeline));

                    }
                }).connect(new InetSocketAddress("localhost", 5783)).sync().channel();

        // 获取关闭 Future
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
    }
}
