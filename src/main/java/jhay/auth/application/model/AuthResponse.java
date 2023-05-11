package jhay.auth.application.model;

import jhay.auth.common.security.jwt.JwtToken;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String assignedTo;
    private Date validTill;

    public AuthResponse(JwtToken jwtToken){
        this.accessToken = jwtToken.getAccessToken();
        this.assignedTo = jwtToken.getUser().getEmail();
        this.validTill = jwtToken.getExpiresAt();
    }
}
