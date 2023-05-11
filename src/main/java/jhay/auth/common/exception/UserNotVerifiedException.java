package jhay.auth.common.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String email) {
        super("User with email : "+email+" is not verified");
    }
}
