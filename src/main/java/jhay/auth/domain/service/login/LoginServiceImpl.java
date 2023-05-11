package jhay.auth.domain.service.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.application.model.LoginRequest;
import jhay.auth.common.event.ForgotPasswordEvent;
import jhay.auth.common.exception.BadCredentialsException;
import jhay.auth.common.exception.UserNotVerifiedException;
import jhay.auth.common.security.jwt.JwtToken;
import jhay.auth.common.security.jwt.JwtTokenRepository;
import jhay.auth.common.utils.EmailUtils;
import jhay.auth.domain.model.User;
import jhay.auth.domain.model.VerificationToken;
import jhay.auth.domain.service.user.UserServiceImpl;
import jhay.auth.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserServiceImpl userService;
    private final ApplicationEventPublisher publisher;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public AuthResponse loginUser(LoginRequest loginRequest){
        User user = userService.getUserByEmail(loginRequest.getEmail());
        if(!user.getIsEnabled()){
            throw new UserNotVerifiedException(user.getEmail());
        }
        if(!user.getPassword().equals(passwordEncoder.encode(loginRequest.getPassword()))){
            throw new BadCredentialsException("Wrong password, Check enter your correct password");
        }
        JwtToken token = tokenRepository.findByUser(user);
        return AuthResponse.builder()
                .accessToken(token.getAccessToken())
                .assignedTo(user.getEmail())
                .validTill(token.getExpiresAt())
                .build();
    }

    @Override
    public String forgotPassword(String email, HttpServletRequest request){
        User user = userService.getUserByEmail(email);
        publisher.publishEvent(new ForgotPasswordEvent(user, EmailUtils.applicationUrl(request)));
        return "Please Check your mail for new Password reset Link";
    }

    @Transactional
    @Override
    public String resetPassword(String email, String password){
        User user = userService.getUserByEmail(email);
        VerificationToken token = verificationTokenRepository.findByUser(user);
        if(token == null || !token.getExpirationTime().before(new Date())){
            throw new IllegalStateException("Invalid Reset Password Token");
        }
        user.setPassword(passwordEncoder.encode(password));
        return "Password Changed, Proceed to Login";
    }
}
