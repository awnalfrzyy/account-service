package strigops.account.features.auth.changePassword.command;

public record ChangePasswordResult(
        String emaail,
        boolean success
        ) {

}
