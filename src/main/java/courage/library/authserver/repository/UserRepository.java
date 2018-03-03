package courage.library.authserver.repository;

import java.util.List;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByEmail(@Param("email") String email);

    UserEntity findByUuid(@Param("uuid") String uuid);

    List<UserEntity> findByLibrary(@Param("library") LibraryEntity library);

    UserEntity findByUuidAndAccountLocked(@Param("uuid") String uuid, @Param("accountLocked") Boolean accountLocked);

    Page<UserEntity> findByLibraryAndAccountLocked(@Param("library") LibraryEntity library, Pageable pageable,
                                                   @Param("accountLocked") Boolean accountLocked);

    UserEntity findByEmailAndAccountLockedAndEnabled(@Param("email") String email,
            @Param("accountLocked") Boolean accountLocked, @Param("enabled") Boolean enabled );

    Page<UserEntity> findByAccountLocked(Pageable pageable, @Param("accountLocked") Boolean accountLocked);

}
