package courage.library.authserver.service.eventandlistner;

import courage.library.authserver.dao.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    @Getter
    @Setter
    private UserEntity user;

    @Getter
    @Setter
    private String eventType;

    public OnRegistrationCompleteEvent(UserEntity user, String eventType) {
        super(user);

        this.user = user;
        this.eventType = eventType;
    }
}
