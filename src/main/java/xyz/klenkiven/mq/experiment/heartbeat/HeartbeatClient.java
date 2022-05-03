package xyz.klenkiven.mq.experiment.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import xyz.klenkiven.mq.experiment.heartbeat.codec.HeartbeatDecoder;
import xyz.klenkiven.mq.experiment.heartbeat.codec.HeartbeatEncoder;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HeartbeatClient {
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
                        // pipeline.addLast(new LoggingHandler());
                        pipeline.addLast(new HeartbeatDecoder());
                        pipeline.addLast(new HeartbeatEncoder());

                        pipeline.addLast(new IdleStateHandler(3, 3, 3));
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                if (evt instanceof IdleStateEvent stateEvent) {
                                    if (stateEvent.state() == IdleState.WRITER_IDLE) {
                                        System.out.println("等待超时，发送心跳包");
                                        ctx.writeAndFlush(new Heartbeat(clientId, (short) 0, new Date()));
                                    }
                                }
                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 5783)).sync().channel();

        new Thread(()->{
            while (true) {
                ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
                byteBuf.writeBytes("Hello World Klen".getBytes(StandardCharsets.UTF_8));
                channel.writeAndFlush(byteBuf);
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "heartbeat-thread").start();

        // 获取关闭 Future
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
    }
}