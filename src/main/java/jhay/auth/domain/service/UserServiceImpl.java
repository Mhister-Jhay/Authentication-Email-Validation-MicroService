package jhay.auth.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import jhay.auth.application.registration.model.RegistrationRequest;
import jhay.auth.application.registration.model.RegistrationResponse;
import jhay.auth.domain.event.RegistrationCompleteEvent;
import jhay.auth.domain.exception.UserAlreadyExistException;
import jhay.auth.domain.exception.UserNotFoundException;
import jhay.auth.domain.model.Role;
import jhay.auth.domain.model.User;
import jhay.auth.domain.repository.UserRepository;
import jhay.auth.domain.security.jwt.JwtAuthProvider;
import jhay.auth.domain.security.jwt.JwtToken;
import jhay.auth.domain.security.jwt.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtAuthProvider authProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registerRequest, HttpServletRequest request){
        verifyUserExistence(registerRequest.getEmail());
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .isEnabled(false)
                .isLocked(false)
                .build();
        User theUser = userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),user.getPassword());
        String accessToken = authProvider.generateToken(authentication);
        String refreshToken = authProvider.generateRefreshToken(authentication);
        JwtToken jwtToken = new JwtToken(accessToken,refreshToken);
        jwtToken.setUser(theUser);
        JwtToken theToken = jwtTokenRepository.save(jwtToken);
        return new RegistrationResponse(theToken);
    }
    @Override
    public User getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(email);
        }else{
            return optionalUser.get();
        }
    }
    private void verifyUserExistence(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException(email);
        }
    }
    @Override
    public String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
