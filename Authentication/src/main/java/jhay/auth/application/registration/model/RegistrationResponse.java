package jhay.auth.application.registration.model;

import jhay.auth.domain.security.jwt.JwtToken;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponse {
    private String accessToken;
    private String assignedTo;
    private Date validTill;

    public RegistrationResponse(JwtToken jwtToken){
        this.accessToken = jwtToken.getAccessToken();
        this.assignedTo = jwtToken.getUser().getEmail();
        this.validTill = jwtToken.getExpiresAt();
    }
}
