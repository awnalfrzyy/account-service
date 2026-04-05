package strigops.account.features.auth.changePassword;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class PasswordChangeEvent extends ApplicationEvent {

    private final String email;

    public PasswordChangeEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
