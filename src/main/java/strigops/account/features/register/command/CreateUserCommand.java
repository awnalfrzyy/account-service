package strigops.account.features.register.command;

public record CreateUserCommand(
        String email,
        String password,
        String username
        ) {

}
