package xyz.klenkiven.mq.experiment.heartbeat.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import xyz.klenkiven.mq.experiment.heartbeat.constant.MqConstant;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Heartbeat Package
 * +--------+--------+---------------+----------------+----------------+
 * | 2 Byte | 2 Byte |     4 Byte    |              8 Byte             |
 * +--------+--------+---------------+----------------+----------------+
 * |  magic |  type  |   client-id   |            timestamp            |
 * +--------+--------+---------------+----------------+----------------+
 */
public class HeartbeatDecoder extends FixedLengthFrameDecoder {

    public HeartbeatDecoder() {
        super(16);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        ByteBuf byteBuf = decode.readBytes(2);
        String magicCode = byteBuf.toString(Charset.defaultCharset());
        if (!MqConstant.BEAT_MAGIC_CODE.equals(magicCode)) {
            return null;
        }
        short type = decode.readShort();
        int clientId = decode.readInt();
        long timestamp = decode.readLong();
        return new Heartbeat(clientId, type, new Date(timestamp));
    }
}
