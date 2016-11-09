import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by ahernandez on 11/8/16.
 */
public class ReceiveTopics {
    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(SendTopics.EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("Type components [KERNEL, PROCESSOR] for listening: ");
        String components = scanner.nextLine();

        System.out.println("Type severity [CRITICAL, NORMAL] for listening: ");
        String severities = scanner.nextLine();

        System.out.println("[x] Waiting messages of type: ");
        for(String component : getParts(components)) {
            for(String severity : getParts(severities)) {
                String routeKey = component.trim().toUpperCase() + "." + severity.trim().toUpperCase();
                System.out.println(routeKey);
                channel.queueBind(queueName, SendTopics.EXCHANGE_NAME, routeKey);
            }
        }

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties
                    properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    private static String[] getParts(String routKeyGeneral) {
        return routKeyGeneral.split(" ");
    }
}
