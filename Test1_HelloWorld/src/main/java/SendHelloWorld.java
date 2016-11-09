import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by ahernandez on 11/8/16.
 */
public class SendHelloWorld {
    public static final String QUEUE_NAME = "jahv.test.rabbitmq.helloworld";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        //Real TCP connection to the broker
        Connection connection = factory.newConnection();

        //Virtual connection (AMPQ connection) inside a connection
        Channel channel = connection.createChannel();

        //To send, we must declare a queue for us to send to; then we can publish a message to the queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World at " + new Date() + "!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[Sender] Sent: '" + message + "'");

        channel.close();
        connection.close();
    }
}
