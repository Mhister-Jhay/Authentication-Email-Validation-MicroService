package jhay.auth.common.security.jwt;

import jhay.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findByRefreshToken(String refreshToken);
    JwtToken findByAccessToken(String accessToken);
    JwtToken findByUser(User user);

}
