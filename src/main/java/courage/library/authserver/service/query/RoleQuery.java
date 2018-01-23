package courage.library.authserver.service.query;

import courage.library.authserver.dto.Role;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface RoleQuery {

    Role findRoleById(String id);
    Page<Role> findRoles(Integer pageNumber, Integer pageSize );

}
