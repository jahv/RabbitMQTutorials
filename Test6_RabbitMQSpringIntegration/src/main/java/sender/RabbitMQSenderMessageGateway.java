package sender;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by ahernandez on 11/9/16.
 */
@MessagingGateway
public interface RabbitMQSenderMessageGateway {

    @Gateway(requestChannel = RabbitMQSenderMessageOutbound.CHANNEL)
    void trigger(String message);
}
