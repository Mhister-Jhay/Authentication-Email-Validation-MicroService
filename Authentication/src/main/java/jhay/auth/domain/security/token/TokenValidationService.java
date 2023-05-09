package jhay.auth.domain.security.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface TokenValidationService {
    @Transactional
    String validateToken(String token, HttpServletRequest request);


    @Transactional
    String requestNewToken(String email, HttpServletRequest request);
}
