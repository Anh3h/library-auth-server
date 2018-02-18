package courage.library.authserver.service.AsyncNotifcation;

import courage.library.authserver.exception.ForbiddenException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void broadcastMessage(final Object object) {
        jmsTemplate.send(session -> {
            try {
                String message = new ObjectMapper().writeValueAsString(object);
                System.out.println(message);
                return session.createObjectMessage(message);
            } catch (IOException e) {
                throw ForbiddenException.create(e.getMessage());
            }
        });
    }

}
