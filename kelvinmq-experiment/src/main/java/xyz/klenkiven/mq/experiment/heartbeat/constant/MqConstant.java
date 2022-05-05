package xyz.klenkiven.mq.experiment.heartbeat.constant;

public final class MqConstant {

    /** MQ Heartbeat Magic Code */
    public static final byte BEAT_MAGIC_CODE = (byte) 0x5A;

    /** FRAME_VERSION */
    public static final byte VERSION = 1;

    /** Frame Compress */
    public static final byte COMPRESS_NO = 0;

    /** MQ Message Magic Code */
    public static final byte[] MSG_MAGIC_CODE = new byte[] {'k', 'k', 'm', 'q'};

    /** Max Frame Length */
    public static final int MAX_FRAME_LENGTH = 4 * 1024 * 1024;
}
