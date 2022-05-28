package xyz.klenkiven.mq.model.builder;

import xyz.klenkiven.mq.constant.MqConstant;
import xyz.klenkiven.mq.model.Frame;

/**
 * FrameBuilder
 */
public final class FrameBuilder {
    private final Frame frame;

    private FrameBuilder() {
        frame =  new Frame();
        frame.setMagic(MqConstant.MAGIC_CODE);
        frame.setMagic(MqConstant.VERSION);
        frame.setCompress(MqConstant.COMPRESS_NO);
    }

    public static FrameBuilder heartbeat() {
        FrameBuilder frameBuilder = new FrameBuilder();
        frameBuilder.frame.setType(MqConstant.FrameType.HEARTBEAT);
        frameBuilder.frame.setPayload(new byte[0]);
        return frameBuilder;
    }

    public static FrameBuilder frame(byte type) {
        FrameBuilder frameBuilder = new FrameBuilder();
        frameBuilder.frame.setType(type);
        return frameBuilder;
    }

    public FrameBuilder payload(byte[] payload) {
        frame.setPayload(payload);
        return this;
    }

    public Frame build() {
        return frame;
    }
}
