package xyz.klenkiven.mq.prototype;

import lombok.Data;

@Data
public class Message {
    public Message() {
    }

    public Message(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    private String topic;
    private String content;
}
