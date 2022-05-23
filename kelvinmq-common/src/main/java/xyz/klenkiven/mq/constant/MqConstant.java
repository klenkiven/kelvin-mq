package xyz.klenkiven.mq.constant;

public class MqConstant {

    /** MQ Heartbeat Magic Code */
    public static final byte MAGIC_CODE = (byte) 0x4B;

    /** FRAME_VERSION */
    public static final byte VERSION = 1;

    /** Frame Compress */
    public static final byte COMPRESS_NO = 0;

    /** Max Frame Length */
    public static final int MAX_FRAME_LENGTH = 4 * 1024 * 1024;

    /** Message Queue Frame Type */
    public static abstract class FrameType {
        public static final int HEARTBEAT = 0;
        public static final int REQUEST = 1;
        public static final int RESPONSE = 2;
    }
}
