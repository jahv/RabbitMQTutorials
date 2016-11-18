package listener;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ahernandez on 11/14/16.
 */
@SpringBootApplication
@ComponentScan
public class ListenerMain implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ListenerMain.class);

    @Autowired
    private DoStuff doStuff;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ListenerMain.class);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, String> queues = doStuff.initExchangesAndQueues();
        //doStuff.startListeners(queues);

        //logger.info("Sleep 3 seconds");
        //Thread.sleep(3000);

        doStuff.send();
        doStuff.startReceiving(queues);
    }
}
