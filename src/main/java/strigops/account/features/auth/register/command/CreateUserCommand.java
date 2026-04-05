package strigops.account.features.auth.register.command;

public record CreateUserCommand(
        String email,
        String password,
        String username
        ) {

}
