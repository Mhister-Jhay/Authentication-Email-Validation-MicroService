package jhay.auth.domain.service.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhay.auth.application.model.AuthResponse;

public interface RequestService {
    AuthResponse validateRequest(String authHeader,
                                 HttpServletRequest request,
                                 HttpServletResponse response);

}
