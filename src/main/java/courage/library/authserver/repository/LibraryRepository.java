package courage.library.authserver.repository;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dto.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, Integer> {

    LibraryEntity findByUuidAndIsAvailable(@Param("uuid")String uuid, @Param("isAvailable") Boolean isAvailable);

    LibraryEntity findByName(@Param("name")String name);

    Page<LibraryEntity> findByIsAvailable(Pageable pageable, @Param("isAvailable") Boolean isAvailable);

}
