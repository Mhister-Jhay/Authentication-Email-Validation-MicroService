package jhay.auth.application.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank(message = "Password must not be blank")
    private String password;
}
