package courage.library.authserver.repository;

import courage.library.authserver.dao.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByIdAndEnabled(@Param("id") String id, @Param("isAvailable") Boolean isAvailable);

    RoleEntity findByName(@Param("name")String name);

    Page<RoleEntity> findByEnabled(Pageable pageable, @Param("isAvailable") Boolean isAvailable);

}
