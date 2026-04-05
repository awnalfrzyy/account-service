package strigops.account.features.auth.changePassword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordChangeListener {

    @EventListener
    @Async
    public void handlePasswordChange(PasswordChangeEvent event) {
        log.info("Sending notification email to:{}", event.getEmail());
    }
}
