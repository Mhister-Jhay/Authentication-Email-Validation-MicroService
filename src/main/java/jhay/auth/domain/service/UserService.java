package jhay.auth.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jhay.auth.application.model.RegistrationRequest;
import jhay.auth.application.model.RegistrationResponse;
import jhay.auth.domain.model.User;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest registerRequest, HttpServletRequest request);

    @Transactional
    String resetPassword(String email, String password);

    User getUserByEmail(String email);

    String applicationUrl(HttpServletRequest request);
}
