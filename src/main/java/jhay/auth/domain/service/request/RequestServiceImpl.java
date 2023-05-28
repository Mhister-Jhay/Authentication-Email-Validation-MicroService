package jhay.auth.domain.service.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.common.exception.UserUnauthorizedException;
import jhay.auth.common.security.jwt.JwtAuthProvider;
import jhay.auth.common.security.jwt.JwtToken;
import jhay.auth.domain.model.User;
import jhay.auth.domain.service.user.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final JwtAuthProvider jwtAuthProvider;
    private UserDetailServiceImpl userService;

    @Override
    public AuthResponse validateRequest(String authHeader,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new UserUnauthorizedException("No authorization header found");
        }
        String refreshToken;
        String accessToken = authHeader.substring(7);
        String userEmail = jwtAuthProvider.extractUsername(accessToken);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userService.loadUserByUsername(userEmail);
            if(jwtAuthProvider.isTokenExpired(accessToken)){
                refreshToken = jwtAuthProvider.getRefreshToken(accessToken);
                JwtToken jwtToken = jwtAuthProvider.generateNewTokens(refreshToken);
                accessToken = jwtToken.getAccessToken();
            }
            if(jwtAuthProvider.isTokenValid(accessToken,user)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        return null;
    }
}
