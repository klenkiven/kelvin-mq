package xyz.klenkiven.mq.experiment.queue_in_server.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import xyz.klenkiven.mq.constant.MqConstant;

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
public class FrameDecoder extends LengthFieldBasedFrameDecoder {

    public FrameDecoder() {
        super(MqConstant.MAX_FRAME_LENGTH,
                5, 4,
                -8, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        checkMagicCode(decode.readByte(), ctx);
        checkProtocolVersion(decode.readByte(), ctx);
        byte type = decode.readByte();
        byte compress = decode.readByte();
        int length = decode.readInt();

        byte[] payload = new byte[length];
        if (length != 0) {
            decode.readBytes(payload);
        }

        // TODO Decompress Payload

        // TODO Resolve Payload
        Object ret = null;
        if (type == MqConstant.FrameType.REQUEST) ret = resolveRequest(payload);
        else if (type == MqConstant.FrameType.RESPONSE) ret = resolveResponse(payload);

        // 释放资源
        ReferenceCountUtil.release(in);
        return ret;
    }

    private Object resolveResponse(byte[] payload) {
        return null;
    }

    private Object resolveRequest(byte[] payload) {
        return null;
    }

    private void checkProtocolVersion(byte version, ChannelHandlerContext ctx) {
        if (version != MqConstant.VERSION){
            ctx.channel().close();
        }
    }

    private void checkMagicCode(byte magicCode, ChannelHandlerContext ctx) {
        if (magicCode != MqConstant.MAGIC_CODE){
            ctx.channel().close();
        }
    }
}
