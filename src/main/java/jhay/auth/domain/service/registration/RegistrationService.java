package jhay.auth.domain.service.registration;

import jakarta.servlet.http.HttpServletRequest;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.application.model.RegistrationRequest;

public interface RegistrationService {
    AuthResponse registerUser(RegistrationRequest registerRequest, HttpServletRequest request);
}
