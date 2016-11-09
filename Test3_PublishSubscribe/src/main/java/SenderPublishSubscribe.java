import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by ahernandez on 11/8/16.
 */
public class SenderPublishSubscribe {

    public static final String EXCHANGE_NAME = "jahv.test.rabbitmq.exchange.publishsubscribe";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Create exchange of type fanout
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        String message = "Publish/Subscribe test at " + new Date();

        //Publish to the exchange, instead of using the 2nd parameter as previous examples
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println("[x] Sent to exchange[" + EXCHANGE_NAME + "]: '" + message + "'");

        channel.close();
        connection.close();
    }
}
