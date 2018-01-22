package courage.library.authserver.service.command;

import courage.library.authserver.dto.Role;

public interface RoleCommand {

    Role createRole( Role role );
    Role updateRole( Role role );
    void deleteRole( String uuid );

}
