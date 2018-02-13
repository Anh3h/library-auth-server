package courage.library.authserver.repository;

import courage.library.authserver.dao.LibraryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, Integer> {

    LibraryEntity findByUuidAndEnabled(@Param("uuid")String uuid, @Param("isAvailable") Boolean isAvailable);

    LibraryEntity findByName(@Param("name")String name);

    LibraryEntity findByUuid(@Param("uuid")String uuid);

    Page<LibraryEntity> findByEnabled(Pageable pageable, @Param("isAvailable") Boolean isAvailable);

}
