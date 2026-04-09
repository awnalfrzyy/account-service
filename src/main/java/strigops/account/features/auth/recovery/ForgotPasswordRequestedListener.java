package strigops.account.features.auth.recovery;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ForgotPasswordRequestedListener {

    @Async("taskExecutor")
    @EventListener
    public void handleForgotPasswordRequest(ForgotPasswordRequestEvent event){
        log.info("--- MOCK EMAIL SENDER ---");
        log.info("Kirim email ke: {}", event.email());
        log.info("Link Reset: http://localhost:8080/reset?token={}", event.token());
        log.info("-------------------------");
    }
}
