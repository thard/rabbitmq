package rabbitmq.prototype;

import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * Created by tharder on 09/04/15.
 */
public class NewTask {

    private final static String QUEUE_NAME = "hello";
    private final static String QUEUE_NAME_DURABLE_QUEUE = "task_queue";

    public static void main(String[] argv)
            throws java.io.IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME_DURABLE_QUEUE, durable(), false, false, null);

        String message = getMessage(argv);

        channel.basicPublish("", QUEUE_NAME_DURABLE_QUEUE,
                            markMessageAsPersistent(),
                            message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();

    }


    /**
     * This is a loose guarantee. If you need a stronger guarantee then you can use publisher confirms.
     * @return
     */
    private static AMQP.BasicProperties markMessageAsPersistent() {
        return MessageProperties.PERSISTENT_TEXT_PLAIN;
    }

    private static boolean durable() {
        return false;
    }

    private static String getMessage(String[] strings){
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
