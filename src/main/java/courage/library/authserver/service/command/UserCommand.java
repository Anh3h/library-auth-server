package courage.library.authserver.service.command;

import courage.library.authserver.dto.User;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
public interface UserCommand {

    User createUser( User user );
    User updateUser( User user );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteUser( String uuid );

}