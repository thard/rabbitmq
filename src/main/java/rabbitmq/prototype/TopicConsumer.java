package rabbitmq.prototype;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class TopicConsumer {
    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("vhost1");
        factory.setUsername("testuser1");
        factory.setPassword("pass1");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeName(), ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeType());
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeName(), "");
            System.out.println(" [*] Waiting for messages.");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                //TODO deserialize
                //TODO use eventManager to process
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received '" + message + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO
        }
    }
}
