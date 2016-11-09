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
public class SendRouting {

    public static final String EXCHANGE_NAME = "jahv.test.rabbitmq.exchange.routing";

    private static final String LOG_LINE = ": Log data at ";

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        System.out.println("Type severity [INFO, ERROR, WARNING] for sending: ");
        String routingKey = scanner.nextLine();
        String message = routingKey + LOG_LINE + new Date();

        //Publishing using a routeKey defined by user
        channel.basicPublish(EXCHANGE_NAME, routingKey.trim().toUpperCase(), null, message.getBytes() );

        System.out.println("[x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
