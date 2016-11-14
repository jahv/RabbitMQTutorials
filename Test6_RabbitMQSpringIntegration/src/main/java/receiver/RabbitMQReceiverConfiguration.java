package receiver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ahernandez on 11/9/16.
 */
@Configuration
public class RabbitMQReceiverConfiguration {

    private static final String EXCHANGE_NAME = "jahv.test.rabbitmq.exchange.rabbitmqspringintegration";
    private static final String QUEUE_NAME = "jahv.test.rabbitmq.queue.rabbitmqspringintegration";

    @Autowired
    private ConnectionFactory rabbitMQConnectionFactory;

    @Bean
    Exchange jahvRabbitMQExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).autoDelete().build();
    }

    @Bean
    public Queue jahvRabbitMQQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding jahvRabbitMQBinding() {
        return BindingBuilder.bind(jahvRabbitMQQueue()).to(jahvRabbitMQExchange()).with("#").noargs();
    }

    @Bean
    public SimpleMessageListenerContainer jahvRabbitMQListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(rabbitMQConnectionFactory);
        simpleMessageListenerContainer.setQueues(jahvRabbitMQQueue());
        simpleMessageListenerContainer.setConcurrentConsumers(2);
        return simpleMessageListenerContainer;
    }

}
