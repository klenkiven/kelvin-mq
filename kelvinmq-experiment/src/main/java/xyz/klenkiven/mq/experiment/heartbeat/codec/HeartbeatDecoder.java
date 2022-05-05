package xyz.klenkiven.mq.experiment.heartbeat.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import xyz.klenkiven.mq.experiment.heartbeat.constant.MqConstant;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;

/**
 * 心跳包
 * 0        8       16       24       32
 * +--------+--------+--------+--------+
 * |  magic | version|  type  |compress|
 * +--------+--------+--------+--------+
 * |              length               |
 * +--------+--------+--------+---------
 * HEAD 8B
 * 1B version 1B type 1B codec 1B compress
 *              4B length
 */
public class HeartbeatDecoder extends LengthFieldBasedFrameDecoder {

    public HeartbeatDecoder() {
        super(MqConstant.MAX_FRAME_LENGTH,
                5, 4,
                -8, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        byte magicCode = decode.readByte();
        byte version = decode.readByte();
        byte type = decode.readByte();
        byte compress = decode.readByte();
        int length = decode.readInt();

        byte[] payload = new byte[length];
        if (length != 0) {
            decode.readBytes(payload);
        }
        // 释放资源
        ReferenceCountUtil.release(in);
        return new Heartbeat(magicCode, version, type, compress, payload);
    }
}
