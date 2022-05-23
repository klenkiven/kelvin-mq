package xyz.klenkiven.mq.model;

import xyz.klenkiven.mq.constant.MqConstant;

/**
 * 传输层包
 *
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
public class Frame {

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

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte getCompress() {
        return compress;
    }

    public void setCompress(byte compress) {
        this.compress = compress;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
