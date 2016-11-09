import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by ahernandez on 11/8/16.
 */
public class SendWorkQueues {

    public static final String QUEUE_NAME = "jahv.test.rabbitmq.worksqueues";

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        String message = "Keep";

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        while(!message.equalsIgnoreCase("stop")) {
            System.out.println("Type STOP to finalize execution.");
            System.out.println("[x] Give instruction (One period '.' equals one second): ");
            message = scanner.nextLine();

            if(!message.equalsIgnoreCase("stop")) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("[x] Sent: '" + message + "'");
            }
        }

        channel.close();
        connection.close();
    }
}
