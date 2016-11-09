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
public class ReceiverPublishSubscribe {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Set the exchange and type
        channel.exchangeDeclare(SenderPublishSubscribe.EXCHANGE_NAME, "fanout");

        //Create a temporary queue and bound to the exchange
        //Every client will create a temporary queue bound to same exchange, since the sender publish
        // to the exchange, and the exchange is fanout, it will deliver the message to al queues bound
        //to it
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, SenderPublishSubscribe.EXCHANGE_NAME, "");

        System.out.println("Queue [" + queueName + "] bound to Exchange [" + SenderPublishSubscribe.EXCHANGE_NAME + "]");
        System.out.println("[x] Waiting for messages.");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties
                    properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        //Consume from temporary queue
        channel.basicConsume(queueName, false, consumer);
    }
}
