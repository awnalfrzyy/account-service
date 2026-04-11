package strigops.account.common.validator;

public class EmailValidator {
    public static final String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String MESSAGE = "Invalid email format";
    public static final String DOMAIN_FORBIDDEN_MESSAGE = "This domain is not registered in our corporate service. Please register your company first.";

    private EmailValidator() {}
}
