package xyz.klenkiven.mq.command;

import java.util.Date;

/**
 * 连接相关命令
 */
public class ConnectionCommand implements Command {
    public static final byte TYPE = 10;

    private final Date timestamp;



    public ConnectionCommand(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    public static class Ok implements Command {
        public static final byte TYPE = 11;
        private final String connectionId;
        private final Date timestamp;

        public Ok(String connectionId) {
            this.connectionId = connectionId;
            this.timestamp = new Date();
        }

        public String getConnectionId() {
            return connectionId;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        @Override
        public byte getType() {
            return TYPE;
        }
    }
}
