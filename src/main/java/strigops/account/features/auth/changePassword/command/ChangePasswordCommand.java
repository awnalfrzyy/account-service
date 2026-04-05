package strigops.account.features.auth.changePassword.command;

public record ChangePasswordCommand(
        String email,
        String oldPassword,
        String newPassword
        ) {

}
