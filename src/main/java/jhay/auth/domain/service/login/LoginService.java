package jhay.auth.domain.service.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.application.model.LoginRequest;

public interface LoginService {
    AuthResponse loginUser(LoginRequest loginRequest);

    String forgotPassword(String email, HttpServletRequest request);

    @Transactional
    String resetPassword(String email, String password);
}
