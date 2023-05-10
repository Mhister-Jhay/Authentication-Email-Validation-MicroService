package jhay.auth.common.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findByRefreshToken(String refreshToken);
    JwtToken findByAccessToken(String accessToken);

}
