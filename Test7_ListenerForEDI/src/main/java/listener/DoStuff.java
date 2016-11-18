package listener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ahernandez on 11/14/16.
 */
@Component
public class DoStuff {

    private static final Logger logger = LoggerFactory.getLogger(DoStuff.class);

    @Autowired
    private ListenerConfig listenerConfig;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    public Map<String, String> initExchangesAndQueues() {
        Map<String, String> queueBound = new LinkedHashMap<>();
        for(String exchange : listenerConfig.getExchanges()) {
            final boolean durable = false;
            final boolean autoDelete = false;
            final Exchange x = new TopicExchange(exchange, durable, autoDelete);
            rabbitAdmin.declareExchange(x);

            String routingKey = "#";

            // create a temp queue to listen for the expected message
            Queue q = rabbitAdmin.declareQueue();
            rabbitAdmin.declareBinding(BindingBuilder.bind(q).to(x).with(routingKey).noargs());
            logger.info("Exchange [{}] bound to Queue [{}] using routing key [{}]", exchange, q.getName(), routingKey);
            queueBound.put(q.getName(), exchange);
        }
        return queueBound;
    }

    public void startReceiving(Map<String, String> queues) throws Exception {

        for(String queue : queues.keySet()) {
            Message m = receive(queue, 10, 100);
            if(m == null) {
                logger.error("Unable to read from {}", queues.get(queue));
                break;
            } else {
                String message = new String(m.getBody(), StandardCharsets.UTF_8);
                logger.info(message);
            }
        }
        logger.info("DONE");
    }

    private Message receive(final String q, int retry, final long msDelay) throws InterruptedException {
        Message m = rabbitTemplate.receive(q);
        if (m != null || retry-- <= 0) {
            return m;
        }
        TimeUnit.MILLISECONDS.sleep(msDelay);
        return receive(q, retry, msDelay);
    }

    public void startListeners(List<String> queues) {
        for(String queue : queues) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
            Object listener = new Object() {
                public void handleMessage(String foo) {
                    System.out.println(foo);
                }
            };
            MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
            container.setMessageListener(adapter);
            container.setQueueNames(queue);
            container.start();
            logger.info("Start listener for Queue [{}]", queue);
        }
    }

    public void send() {
        for(String exchange : listenerConfig.getExchanges()) {
            logger.info("[x] Sent to {}", exchange);
            rabbitTemplate.convertAndSend(exchange, "#", "Hello " + exchange + "!");
            if(exchange.contains("2")) {
                break;
            }
        }
    }
}
