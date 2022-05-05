package xyz.klenkiven.mq.prototype;

import xyz.klenkiven.mq.prototype.impl.BrokerImpl;

import java.util.Collection;
import java.util.Scanner;

public class Main {
    private static final Broker BROKER = new BrokerImpl();

    public static void main(String[] args) {
        System.out.println("欢迎使用消息命令行工具");
        Scanner sc = new Scanner(System.in);
        for (;;) {
            System.out.print("Command > ");
            String[] command = sc.nextLine().split(" ");
            try {
                switch (command[0].toUpperCase()) {
                    // 输入回车啥也不做
                    case "" -> {}
                    // 退出程序
                    case "QUIT" -> {
                        BROKER.shutdown();
                        return;
                    }
                    case "LIST" -> listCommand(command);
                    case "CREATE" -> createCommand(command);
                    case "DELETE" -> deleteCommand(command);
                    case "HELP" -> helpCommand(command);
                    default -> connectionCommand(command);
                }
            } catch (Exception e) {
                System.out.println("命令有误，请检查");
                e.printStackTrace();
            }
        }
    }

    private static void listCommand(String[] command) {
        String target = command[1].toUpperCase();
        if ("CONNECTIONS".equals(target)) {
            Collection<Connection> connections = BROKER.listConnectionId();
            if (connections == null || connections.size() == 0) {
                System.out.println("尚无连接建立");
                return;
            }
            for (Connection connection : connections) {
                System.out.println("===================================================");
                System.out.println("Connection ID: " + connection.getConnectionId());
                System.out.println("Connection Subscribe Topic: " + connection.subscribedTopicSet());
            }
            System.out.println("===================================================");
        } else {
            System.out.println("命令有误，请检查");
        }
    }

    private static void createCommand(String[] command) {
        String target = command[1].toUpperCase();
        String param = null;
        if (command.length > 3) {
            System.out.println("命令有误，请检查");
            return;
        } else if (command.length == 3) {
            param = command[2];
        }

        if ("CONNECTION".equalsIgnoreCase(target)) {
            Connection connection = null;
            if (param == null) {
                connection = BROKER.createConnection();
            } else {
                connection = BROKER.createConnection(param);
            }

            if (connection != null) {
                System.out.println("连接创建成功");
                System.out.println("===================================================");
                System.out.println("Connection ID: " + connection.getConnectionId());
                System.out.println("Connection Running: " + connection.isRunning());
                System.out.println("===================================================");
            } else {
                System.out.println("连接创建失败");
            }
        }
    }

    private static void deleteCommand(String[] command) {
        if (command.length != 3) {
            System.out.println("命令有误，请检查");
            return;
        }
        String target = command[1].toUpperCase();
        String param = command[2];
        if ("CONNECTION".equalsIgnoreCase(target)) {
            if (!BROKER.deleteConnection(param)) {
                System.out.println("此连接不存在或者连接已被删除");
            } else {
                System.out.println("Connection: " + param + " 删除成功");
            }
        } else {
            System.out.println("命令有误，请检查");
        }
    }

    private static void connectionCommand(String[] command) {
        String connectionId = command[0];
        if (command.length <= 2) {
            System.out.println("命令有误，请检查");
            return;
        }
        Connection connection = BROKER.getConnection(connectionId);
        if (connection == null) {
            System.out.println("连接 ["+connectionId+"] 不存在");
            return;
        }

        String cmd = command[1].toUpperCase();
        switch (cmd) {
            case "SUB" -> connectionSubscribeCommand(connection, command[2]);
            case "PUB" -> connectionPublishCommand(connection, command[2], command[3]);
        }
    }

    private static void connectionSubscribeCommand(Connection connection, String topic) {
        if (connection.subscribe(topic)) {
            System.out.println("连接 ["+connection.getConnectionId()+"] 订阅了 ["+topic+"]");
        } else {
            System.out.println("订阅失败");
        }
    }

    private static void connectionPublishCommand(Connection connection, String topic, String message) {
        connection.publish(topic, new Message(topic, message));
        System.out.println("连接 ["+connection.getConnectionId()+"] 在 ["+topic+"] 发布了消息");
    }

    private static void helpCommand(String[] command) {

    }

}
