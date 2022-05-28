package xyz.klenkiven.mq.model;

import io.netty.channel.Channel;

import java.util.Date;

/**
 * 客户端连接
 */
public class Connection {

    private String connectionId;

    private Channel channel;

    private Date createTime;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
