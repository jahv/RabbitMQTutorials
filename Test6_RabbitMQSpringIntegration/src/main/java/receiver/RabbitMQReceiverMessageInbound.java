package receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.support.Transformers;

/**
 * Created by ahernandez on 11/9/16.
 */
@Configuration
public class RabbitMQReceiverMessageInbound {

    @Autowired
    private RabbitMQReceiverConfiguration rabbitMQReceiverConfiguration;

    @Bean
    public IntegrationFlow inboundMessageFlow() {
        return IntegrationFlows.from(
                Amqp.inboundAdapter(rabbitMQReceiverConfiguration.jahvRabbitMQListenerContainer()))
                //.transform(Transformers.objectToString())
                .log()
                .handle("inboundMessage", "handle")
                .get();
    }

}
