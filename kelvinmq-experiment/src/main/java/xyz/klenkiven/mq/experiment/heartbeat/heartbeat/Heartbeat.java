package xyz.klenkiven.mq.experiment.heartbeat.heartbeat;

import lombok.Data;

import java.util.Date;

/**
 * 心跳包
 * 0        8       16       24       32
 * +--------+--------+--------+--------+
 * |  magic | version|  type  |compress|
 * +--------+--------+--------+--------+
 * |              length               |
 * +--------+--------+--------+---------
 *
 * 1B version 1B type 1B codec 1B compress
 *              4B length
 */
@Data
public class Heartbeat {

    /** 魔数 */
    private byte magic;

    /** Protocol Version */
    private byte version;

    /** Frame Type */
    private int type;

    /** compress type */
    private byte compress;

    /** Payload */
    private byte[] payload;

    public Heartbeat(byte magic, byte version, int type, byte compress, byte[] payload) {
        this.magic = magic;
        this.version = version;
        this.type = type;
        this.compress = compress;
        this.payload = payload;
    }
}
