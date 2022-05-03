package xyz.klenkiven.mq.experiment.heartbeat.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.klenkiven.mq.experiment.heartbeat.constant.MqConstant;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;

import java.nio.charset.StandardCharsets;

/**
 * Heartbeat Package
 * +--------+--------+---------------+----------------+----------------+
 * | 2 Byte | 2 Byte |     4 Byte    |              8 Byte             |
 * +--------+--------+---------------+----------------+----------------+
 * |  magic |  type  |   client-id   |            timestamp            |
 * +--------+--------+---------------+----------------+----------------+
 */
public class HeartbeatEncoder extends MessageToByteEncoder<Heartbeat> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Heartbeat msg, ByteBuf out) throws Exception {
        out.writeBytes(MqConstant.BEAT_MAGIC_CODE.getBytes(StandardCharsets.UTF_8));
        out.writeShort(msg.getType());
        out.writeInt(msg.getClientId());
        out.writeLong(msg.getTimestamp().getTime());
    }
}
