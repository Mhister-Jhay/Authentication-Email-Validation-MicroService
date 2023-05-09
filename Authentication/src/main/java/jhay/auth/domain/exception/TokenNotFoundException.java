package jhay.auth.domain.exception;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String token) {
        super("Invalid Verification Token :"+token);
    }
}
