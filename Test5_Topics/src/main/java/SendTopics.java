import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by ahernandez on 11/8/16.
 */
public class SendTopics {

    public static final String EXCHANGE_NAME = "jahv.test.rabbitmq.exchange.topics";
    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //Define topic exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        System.out.println("Type component [KERNEL, PROCESSOR] for sending: ");
        String component = scanner.nextLine();

        System.out.println("Type severity [CRITICAL, NORMAL] for sending: ");
        String severity = scanner.nextLine();

        String routingKey = component.trim().toUpperCase() + "." + severity.trim().toUpperCase();
        String message = "[" + component + "][" + severity + "] : sent at " + new Date();

        //Publishing using a routeKey defined by user
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());

        System.out.println("[x] Sent '" + message + "'");

        channel.close();
        connection.close();

    }

}
