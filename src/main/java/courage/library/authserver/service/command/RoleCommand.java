package courage.library.authserver.service.command;

import courage.library.authserver.dto.Role;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface RoleCommand {

    Role createRole( Role role );
    Role updateRole( Role role );
    void deleteRole( String uuid );

}
