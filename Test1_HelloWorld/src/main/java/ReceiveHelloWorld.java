import java.io.IOException;
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
public class ReceiveHelloWorld {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(SendHelloWorld.QUEUE_NAME, false, false, false, null);

        System.out.println(" [Receiver] Waiting for messages.");

        //Define a consumer to handle the incoming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties
                    properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[Receiver] Received '" + message + "'");

            }
        };

        //Execute the instruction for listen on QUE_NAME, and handle it through consumer
        channel.basicConsume(SendHelloWorld.QUEUE_NAME, true, consumer);
    }
}
