package jhay.auth.domain.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String email) {
        super("User with email : "+ email+" already exist");
    }
}
