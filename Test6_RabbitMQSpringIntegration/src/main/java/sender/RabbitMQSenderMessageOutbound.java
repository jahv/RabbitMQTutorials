package sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;

/**
 * Created by ahernandez on 11/9/16.
 */
@Configuration
public class RabbitMQSenderMessageOutbound {

    public static final String CHANNEL = "jahvRabbitMQMessageChannel";

    @Autowired
    private RabbitMQSenderConfiguration rabbitMQSenderConfiguration;

    @Bean
    public IntegrationFlow startFlow() {
        return IntegrationFlows.from(CHANNEL)
                .handle(Amqp.outboundAdapter(rabbitMQSenderConfiguration.jahvRabbitMQTemplate()))
                .get();
    }
}
