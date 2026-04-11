package strigops.account.common.validator;

public class UsernameValidator {
    public static final String REGEX = "^[a-zA-Z][a-zA-Z0-9_]{2,19}$";
    public static final String MESSAGE = "Username must be 3-20 characters, start with a letter, and only contain letters, numbers, or underscore";

    private UsernameValidator() {}
}
