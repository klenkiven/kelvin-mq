package xyz.klenkiven.mq.prototype;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageHandler implements Runnable {

    private final BlockingQueue<Message> messages;

    private final String connectionId;

    private final String topic;

    private volatile boolean _START = true;

    public MessageHandler(String topic, String connectionId) {
        messages = new LinkedBlockingQueue<>();
        this.connectionId = connectionId;
        this.topic = topic;
    }

    @Override
    public void run() {
        log.debug("[{}-{}-handler] 正在监听中...", connectionId, topic);
        while (_START) {
            try {
                Message message = messages.poll(3, TimeUnit.SECONDS);
                if (message != null) {
                    log.debug("[{}-{}-handler] 来自主题: {}, 消息: {}", connectionId, topic, message.getTopic(), message.getContent());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.debug("[{}-{}-handler] 结束监听", connectionId, topic);
    }

    /**
     * 添加新来的消息
     */
    public void receiveMessage(Message message) {
        messages.offer(message);
    }

    /**
     * 解除消息监听
     */
    public void shutdown() {
        _START = false;
        messages.clear();
    }
}
