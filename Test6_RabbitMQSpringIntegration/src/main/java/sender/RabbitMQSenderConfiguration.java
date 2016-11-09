package sender;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ahernandez on 11/9/16.
 */
@Configuration
public class RabbitMQSenderConfiguration {

    private static final String EXCHANGE_NAME = "jahv.test.rabbitmq.exchange.rabbitmqspringintegration";

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Bean
    TopicExchange jahvRabbitMQExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public RabbitTemplate jahvRabbitMQTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setExchange(EXCHANGE_NAME);
        //rabbitTemplate.setRoutingKey();
        rabbitTemplate.setConnectionFactory(rabbitConnectionFactory);
        return rabbitTemplate;
    }
}
