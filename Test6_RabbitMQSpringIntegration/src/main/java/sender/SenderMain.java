package sender;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;

/**
 * Created by ahernandez on 11/9/16.
 */
@SpringBootApplication
@IntegrationComponentScan("sender")
@ComponentScan("sender")
public class SenderMain {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SenderMain.class);

        RabbitMQSenderMessageGateway rabbitMQSenderMessageGateway = ctx.getBean(RabbitMQSenderMessageGateway.class);
        rabbitMQSenderMessageGateway.trigger("Hola Mundo");
    }

}
