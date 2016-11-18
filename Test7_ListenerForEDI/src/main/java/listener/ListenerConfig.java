package listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ahernandez on 11/14/16.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ListenerConfig {

    private List<String> exchanges = new ArrayList<String>();

    public List<String> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<String> exchanges) {
        this.exchanges = exchanges;
    }
}
