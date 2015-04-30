package rabbitmq.prototype;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class TopicProducer {

    private static ConnectionFactory connectionFactory;

    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = obtainConnection();

            Channel channel = connection.createChannel();
            channel.exchangeDeclare(ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeName(), ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeType());

            String message = "Hello Topic World";

            final String routingKeyIgnoredForFanoutAndThusEmpty = "";
            channel.basicPublish(ExchangeType.CUSTOMER_EVENT_FANOUT.getExchangeName(), routingKeyIgnoredForFanoutAndThusEmpty, null, message.getBytes());

            System.out.println(" [x] Sent '" + message);

            channel.close();
            connection.close();

        } catch (IOException e) {
            //TODO throw
            e.printStackTrace();
        }
    }

    private static Connection obtainConnection() throws IOException {
        return createConnectionFactory().newConnection();
    }

    private static ConnectionFactory createConnectionFactory() {
        if (connectionFactory == null) {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("localhost");
            factory.setVirtualHost("vhost1");
            factory.setUsername("testuser1");
            factory.setPassword("pass1");
            connectionFactory = factory;
        }
        return connectionFactory;
    }
}
