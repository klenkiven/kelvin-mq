package xyz.klenkiven.mq.model;

/**
 * 消息队列消息传输实体
 */
public class Message {

    /** Action Type */
    private int action;

    /** Payload */
    private byte[] payload;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
