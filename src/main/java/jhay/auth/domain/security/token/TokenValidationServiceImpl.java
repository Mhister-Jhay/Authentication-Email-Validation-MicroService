package jhay.auth.domain.security.token;

import jakarta.servlet.http.HttpServletRequest;
import jhay.auth.domain.event.RegistrationCompleteEvent;
import jhay.auth.domain.exception.TokenExpiredException;
import jhay.auth.domain.exception.TokenNotFoundException;
import jhay.auth.domain.exception.UserAlreadyVerifiedException;
import jhay.auth.domain.model.User;
import jhay.auth.domain.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService{
    private final VerificationTokenRepository tokenRepository;
    private final UserServiceImpl userService;
    private final ApplicationEventPublisher publisher;
    @Transactional
    @Override
    public String validateToken(String token, HttpServletRequest request){
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken == null){
            throw new TokenNotFoundException(token);
        }
        User user = verificationToken.getUser();
        if(user.getIsEnabled()){
            throw new UserAlreadyVerifiedException(user.getEmail());
        }
        if(verificationToken.getExpirationTime().before(new Date())){
            return "Please click on the link to get a new verification mail : "+
                    userService.applicationUrl(request)+"/register/request-new-token?email="+user.getEmail();
        }
        user.setIsEnabled(true);
        if(user.getIsEnabled()){
            return "Email Verified Successfully, Please proceed to Login.";
        }else{
            return "Invalid Token";
        }
    }
    @Transactional
    @Override
    public String requestNewToken(String email, HttpServletRequest request){
        User user = userService.getUserByEmail(email);
        if(user.getIsEnabled()){
            throw new UserAlreadyVerifiedException(email);
        }
        VerificationToken verificationToken =
                tokenRepository.findByUser(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user,userService.applicationUrl(request)));
        tokenRepository.delete(verificationToken);
        return "Please check your mail for new Verification Link";
    }

    public void saveVerificationToken(String token, User user){
        VerificationToken verificationToken = new VerificationToken(token,user);
        tokenRepository.save(verificationToken);
    }
}
