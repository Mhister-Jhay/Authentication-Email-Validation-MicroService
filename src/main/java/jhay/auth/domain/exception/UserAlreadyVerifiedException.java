package jhay.auth.domain.exception;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String userEmail) {
        super("This user with email :"+userEmail+" is already verified");
    }
}
