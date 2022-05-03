package xyz.klenkiven.mq.experiment.heartbeat.heartbeat;

import lombok.Data;

import java.util.Date;

/**
 * 心跳包
 */
@Data
public class Heartbeat {
    private int clientId;
    private short type;
    private Date timestamp;

    public Heartbeat(int clientId, short type, Date timestamp) {
        this.clientId = clientId;
        this.type = type;
        this.timestamp = timestamp;
    }
}
