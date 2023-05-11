package jhay.auth.domain.service.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface TokenValidationService {
    @Transactional
    String validateToken(String token, HttpServletRequest request);

    @Transactional
    String requestNewVerificationToken(String email, HttpServletRequest request);

    @Transactional
    String requestForgotPasswordToken(String email, HttpServletRequest request);

    String validatePasswordToken(String token, HttpServletRequest request);

}
