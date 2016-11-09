package receiver;

import org.springframework.stereotype.Service;

/**
 * Created by ahernandez on 11/9/16.
 */
@Service
public class InboundMessage {

    public void handle(String message) {
        System.out.println("Received: [" + message + "]");
    }
}
