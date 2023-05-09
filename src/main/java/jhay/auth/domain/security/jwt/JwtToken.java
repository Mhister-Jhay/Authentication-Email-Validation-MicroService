package jhay.auth.domain.security.jwt;

import jakarta.persistence.*;
import jhay.auth.domain.model.User;
import jhay.auth.domain.utils.DateUtils;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jwt_token")
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String accessToken;
    @Column(nullable = false, unique = true)
    private String refreshToken;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private Date generatedAt;
    @Column(nullable = false)
    private Date expiresAt;
    private Date refreshedAt;
    public JwtToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.generatedAt = new Date();
        this.expiresAt = DateUtils.getExpirationDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JwtToken jwtToken = (JwtToken) o;
        return getId() != null && Objects.equals(getId(), jwtToken.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
