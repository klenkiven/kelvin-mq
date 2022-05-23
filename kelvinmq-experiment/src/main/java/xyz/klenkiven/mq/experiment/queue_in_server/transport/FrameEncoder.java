package xyz.klenkiven.mq.experiment.queue_in_server.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.klenkiven.mq.experiment.heartbeat.constant.MqConstant;
import xyz.klenkiven.mq.experiment.heartbeat.heartbeat.Heartbeat;
import xyz.klenkiven.mq.model.Frame;

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
public class FrameEncoder extends MessageToByteEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) throws Exception {
        out.writeByte(MqConstant.BEAT_MAGIC_CODE);
        out.writeByte(MqConstant.VERSION);
        out.writeByte(msg.getType());
        out.writeByte(msg.getCompress());
        if (msg.getPayload() != null) {
            out.writeInt(msg.getPayload().length);
            out.writeBytes(msg.getPayload());
        } else {
            out.writeInt(0);
        }
    }
}
