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
public class ReceiveRouting {
    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(SendRouting.EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("Type severities [INFO, ERROR, WARNING] for listening: ");
        String routKeyGeneral = scanner.nextLine();

        for(String routeKey : getRouteKeys(routKeyGeneral)) {
            channel.queueBind(queueName, SendRouting.EXCHANGE_NAME, routeKey.trim().toUpperCase());
        }

        System.out.println("[x] Waiting messages of type: " + routKeyGeneral);

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

    private static String[] getRouteKeys(String routKeyGeneral) {
        return routKeyGeneral.split(" ");
    }
}
