package jhay.auth.domain.service.user;

import jakarta.transaction.Transactional;
import jhay.auth.domain.model.User;

public interface UserService {

    User getUserByEmail(String email);

    void verifyUserExistence(String email);

    User saveUser(User user);
}
