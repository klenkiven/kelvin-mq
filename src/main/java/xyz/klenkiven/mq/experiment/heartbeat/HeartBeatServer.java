package xyz.klenkiven.mq.experiment.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import xyz.klenkiven.mq.experiment.heartbeat.codec.HeartbeatDecoder;
import xyz.klenkiven.mq.experiment.heartbeat.codec.HeartbeatEncoder;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HeartBeatServer {
    private static final int serverId = -1;

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup child = new NioEventLoopGroup();
        new ServerBootstrap()
                .group(boss, child)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // pipeline.addLast(new LoggingHandler());
                        // 显示当前连接状态的 Handler
                        // 如果发生超时事件就会发出 超时事件
                        pipeline.addLast(new IdleStateHandler(
                                        30,
                                        60,
                                        60,
                                        TimeUnit.SECONDS));
                        pipeline.addLast(new HeartbeatTimeoutHandler());

                        pipeline.addLast(new HeartbeatDecoder());
                        pipeline.addLast(new HeartbeatEncoder());

                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                Heartbeat heartbeat = (Heartbeat) msg;
                                log.debug("收到：{}的心跳包", ((Heartbeat) msg).getClientId());
                                channel.writeAndFlush(new Heartbeat(serverId, (short) 1, new Date()));
                            }
                        });
                    }
                }).bind(5783);
    }
}
