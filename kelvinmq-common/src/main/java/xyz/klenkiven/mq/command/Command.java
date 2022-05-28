package xyz.klenkiven.mq.command;

import com.alibaba.fastjson.JSON;
import xyz.klenkiven.mq.constant.MqConstant;
import xyz.klenkiven.mq.model.Frame;
import xyz.klenkiven.mq.model.builder.FrameBuilder;

public interface Command {

    byte getType();

    default Frame toFrame() {
        return FrameBuilder.frame(getType())
                .payload(JSON.toJSONBytes(this))
                .build();
    }

    static Command toCommand(byte type, byte[] payload) {
        if (type == ConnectionCommand.TYPE) {
            return JSON.parseObject(payload, ConnectionCommand.class);
        } else if (type == ConnectionCommand.Ok.TYPE) {
            return JSON.parseObject(payload, ConnectionCommand.Ok.class);
        }

        return null;
    }
}
