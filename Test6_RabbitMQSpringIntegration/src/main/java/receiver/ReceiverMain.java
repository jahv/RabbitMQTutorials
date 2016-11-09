package receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;

/**
 * Created by ahernandez on 11/9/16.
 */
@SpringBootApplication
@IntegrationComponentScan("receiver")
@ComponentScan("receiver")
public class ReceiverMain {

    public static void main(String[] args) {
        SpringApplication.run(ReceiverMain.class, args);
    }
}
