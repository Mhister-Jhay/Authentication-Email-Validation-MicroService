package jhay.auth.domain.service.user;

import jakarta.transaction.Transactional;
import jhay.auth.common.exception.UserAlreadyExistException;
import jhay.auth.common.exception.UserNotFoundException;
import jhay.auth.domain.model.User;
import jhay.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(email);
        }else{
            return optionalUser.get();
        }
    }
    @Override
    public void verifyUserExistence(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException(email);
        }
    }
    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
