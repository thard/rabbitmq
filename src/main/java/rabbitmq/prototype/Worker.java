package rabbitmq.prototype;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * Created by tharder on 09/04/15.
 */
public class Worker {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = resendIfAWorkerIsKilledAndMessageCouldNotBeSent();

        channel.basicConsume(QUEUE_NAME, autoAck, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '" + message + "'");
            doWork(message);
            System.out.println(" [x] Done");

            resendIfWorkerWasKilledDuringProcessing(channel, delivery);

        }
    }

    /**
     * If you miss the ack, messages will be redelivered when the client (NewTask) quits,
     * but the successful messages will be kept in memory of RabbitMQ and eat it up.
     *
     * @param channel
     * @param delivery
     * @throws IOException
     */
    private static void resendIfWorkerWasKilledDuringProcessing(Channel channel, QueueingConsumer.Delivery delivery) throws IOException {
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }

    private static boolean resendIfAWorkerIsKilledAndMessageCouldNotBeSent() {
        return false;
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }

}