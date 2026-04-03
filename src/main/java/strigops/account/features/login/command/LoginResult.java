package strigops.account.features.login.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResult {

    private UUID userId;
    private String email;
    private String token;
}
