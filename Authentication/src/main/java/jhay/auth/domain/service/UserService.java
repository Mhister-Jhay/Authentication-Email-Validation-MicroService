package jhay.auth.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import jhay.auth.application.registration.model.RegistrationRequest;
import jhay.auth.application.registration.model.RegistrationResponse;
import jhay.auth.domain.model.User;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest registerRequest, HttpServletRequest request);

    User getUserByEmail(String email);

    String applicationUrl(HttpServletRequest request);
}
