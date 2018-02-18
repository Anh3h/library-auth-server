package courage.library.authserver.service.AsyncNotifcation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver<T> {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Environment env;

    private static final String QUEUE = "transaction-server-queue";

    /*@JmsListener(destination = QUEUE)
    public T receiveMessage() {
        System.out.println("Hello");
        System.out.println( jmsTemplate.receiveAndConvert(env.getProperty("jms.queue")) );
        return null;
    }*/

}
