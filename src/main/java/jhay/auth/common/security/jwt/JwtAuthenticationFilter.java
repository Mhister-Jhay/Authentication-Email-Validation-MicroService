package jhay.auth.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhay.auth.domain.model.User;
import jhay.auth.domain.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthProvider authProvider;
    private final UserDetailServiceImpl userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String userEmail;
        String accessToken;
        String refreshToken;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        accessToken = authHeader.substring(7);
        userEmail = authProvider.extractUsername(accessToken);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userService.loadUserByUsername(userEmail);
            if(authProvider.isTokenExpired(accessToken)){
                refreshToken = authProvider.getRefreshToken(accessToken);
                JwtToken jwtToken = authProvider.generateNewTokens(refreshToken);
                accessToken = jwtToken.getAccessToken();
                response.setHeader("Authorization","Bearer "+accessToken);
            }
            if(authProvider.isTokenValid(accessToken,user)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
