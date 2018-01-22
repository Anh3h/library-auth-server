package courage.library.authserver.service.query;

import courage.library.authserver.dto.Role;
import org.springframework.data.domain.Page;

public interface RoleQuery {

    Role findRoleById(String id);
    Page<Role> findRoles(Integer pageNumber, Integer pageSize );

}
