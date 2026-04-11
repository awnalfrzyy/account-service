package strigops.account.common.validator;

public class PasswordValidator {
    public static final String REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String MESSAGE = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character";

    private PasswordValidator(){}
}
