package xyz.klenkiven.mq.experiment.queue_in_server.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import xyz.klenkiven.mq.command.Command;
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
    private static final int HEADER_LENGTH = 8;

    public FrameDecoder() {
        super(MqConstant.MAX_FRAME_LENGTH,
                4, 4,
                -8, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        checkMagicCode(decode.readByte(), ctx);
        checkProtocolVersion(decode.readByte(), ctx);
        byte type = decode.readByte();
        System.out.println("获取到类型：" + type);
        byte compress = decode.readByte();
        int length = decode.readInt();

        int payloadLength = length - HEADER_LENGTH;
        byte[] payload = new byte[payloadLength];
        if (payloadLength != 0) {
            decode.readBytes(payload);
        }

        if (type == MqConstant.FrameType.HEARTBEAT) {
            ReferenceCountUtil.release(in);
            return null;
        }

        // TODO Decompress

        // 转换对象 && 释放资源
        Object ret = Command.toCommand(type, payload);
        ReferenceCountUtil.release(in);
        return ret;
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
