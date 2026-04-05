package strigops.account.features.auth.login.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCommand {

    private String email;
    private String password;
}
