import java.io.IOException;
import java.util.Date;
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
public class ReceiveWorkQueues {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //If another consumer dies, and has messages assigned to it,
        // this one will receive one message at a time
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties
                    properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Received at " + new Date() + ": '" + message + "'");

                try {
                    process(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Process done at " + new Date());
                    //Sending the flag for acknowledgment and mark it as completed
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }


        };

        System.out.println("[x] Waiting for messages:");
        //Set the autoAck to false to avoid lose messages in case one consumer is stopped/killed
        boolean autoAck = false;
        channel.basicConsume(SendWorkQueues.QUEUE_NAME, autoAck, consumer);
    }

    private static void process(String message) throws InterruptedException {
        for(char c : message.toCharArray()) {
            if(c == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
