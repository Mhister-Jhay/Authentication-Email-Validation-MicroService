package jhay.auth.common.security.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface TokenValidationService {
    @Transactional
    String validateToken(String token, HttpServletRequest request);


    @Transactional
    String requestNewToken(String email, HttpServletRequest request);

    String forgotPassword(String email, HttpServletRequest request);

    String validatePasswordToken(String token, HttpServletRequest request);

}
