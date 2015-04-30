package rabbitmq.prototype;

import com.cedarsoftware.util.io.JsonReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class QueueConsumer {

    private final static String QUEUE_NAME_DURABLE_QUEUE = "event.queue";

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("vhost2");
        factory.setUsername("testuser2");
        factory.setPassword("pass2");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;

        channel.queueDeclare(QUEUE_NAME_DURABLE_QUEUE, durable, false, false, null);

        int prefetchCount = 1;

        fairLoadForAllWorkers(channel, prefetchCount);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = resendIfAWorkerIsKilledAndMessageCouldNotBeSent();

        channel.basicConsume(QUEUE_NAME_DURABLE_QUEUE, autoAck, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '" + message + "'");
            //doWork(message);
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


    private static void fairLoadForAllWorkers(Channel channel, int prefetchCount) throws IOException {
        channel.basicQos(prefetchCount);
    }

    private static boolean resendIfAWorkerIsKilledAndMessageCouldNotBeSent() {
        return false;
    }

    /**
     * This method would do serious work, here is where our System initiates the necessary
     * steps to execute a Task.
     *
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException {

        try {
            String message = (String) JsonReader.jsonToJava(task.replaceAll("\n", "").trim());
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
