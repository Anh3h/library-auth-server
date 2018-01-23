package courage.library.authserver.repository;

import courage.library.authserver.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmail(@Param("email") String email);

    UserEntity findByUuidAndIsAvailable(@Param("uuid") String uuid, @Param("isAvailable") Boolean isAvailable);

    UserEntity findByEmailAndIsAvailable(@Param("email") String email, @Param("isAvailable") Boolean isAvailable);

    Page<UserEntity> findByIsAvailable(Pageable pageable, @Param("isAvailable") Boolean isAvailable);

}
