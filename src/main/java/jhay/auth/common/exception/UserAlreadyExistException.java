package jhay.auth.common.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String email) {
        super("User with email : "+ email+" already exist");
    }
}
