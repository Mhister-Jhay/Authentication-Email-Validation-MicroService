package jhay.auth.repository;

import jhay.auth.domain.model.User;
import jhay.auth.domain.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
