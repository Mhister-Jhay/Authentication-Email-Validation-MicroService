package jhay.auth.common.event;

import jhay.auth.domain.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
    public RegistrationEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
