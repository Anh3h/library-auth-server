package courage.library.authserver.repository;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dao.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<VerificationTokenEntity, String> {

    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUser(UserEntity user);

}
