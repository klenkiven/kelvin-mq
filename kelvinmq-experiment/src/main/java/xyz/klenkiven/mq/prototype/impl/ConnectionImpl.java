package xyz.klenkiven.mq.prototype.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.klenkiven.mq.prototype.Broker;
import xyz.klenkiven.mq.prototype.Connection;
import xyz.klenkiven.mq.prototype.Message;
import xyz.klenkiven.mq.prototype.MessageHandler;

import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class ConnectionImpl implements Connection {

    private final Broker brokerProxy;

    private final String connectionId;

    private final ConcurrentHashMap<String, MessageHandler> localMessage = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    private volatile boolean _START = true;

    public ConnectionImpl(String connectionId, Broker brokerProxy) {
        this.brokerProxy = brokerProxy;
        this.connectionId = connectionId;

        start();
    }

    /**
     * 启动整个客户端程序
     */
    private void start() {
        new Thread(() -> {
            log.debug("[{}] 客户端正在运行中", connectionId);
            while (_START) {
                try {
                    Runnable task = tasks.poll(3, TimeUnit.SECONDS);
                    if (task != null) {
                        executor.execute(task);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.debug("[{}] 客户端关闭", connectionId);
        }, "client-listener-thread").start();
    }

    /**
     * 客户端发布消息
     */
    public void publish(String topic, Message message) {
        brokerProxy.handlePublish(topic, message);
    }

    @Override
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public Set<String> subscribedTopicSet() {
        return localMessage.keySet();
    }

    @Override
    public boolean isRunning() {
        return _START;
    }

    /**
     * [回调方法] 服务端推送消息
     */
    @Override
    public void handleMessage(String topic, Message message) {
        if (!localMessage.containsKey(topic)) {
            log.error("[{}] 接收到未订阅的消息", connectionId);
        }
        MessageHandler messages = localMessage.get(topic);
        messages.receiveMessage(message);
    }

    /**
     * 客户端订阅消息
     */
    @Override
    public boolean subscribe(String topic) {
        boolean subscribe = brokerProxy.handleSubscribe(connectionId, topic);
        if (subscribe) {
            MessageHandler messageHandler = new MessageHandler(topic, connectionId);
            localMessage.put(topic, messageHandler);
            tasks.offer(messageHandler);
        }
        return subscribe;
    }

    /**
     * 客户端解除订阅消息
     */
    @Override
    public void unsubscribe(String topic) {
        localMessage.remove(topic);
        brokerProxy.handleUnsubscribe(connectionId, topic);
    }

    /**
     * 关闭客户端
     */
    @Override
    public void shutdown() {
        // 关闭所有的消息监听器
        for (MessageHandler handler : localMessage.values()) {
            handler.shutdown();
        }
        // 关闭连接线程
        _START = false;

        // 关闭线程池
        executor.shutdown();
    }
}
