package courage.library.authserver.service.command;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.text.ParseException;

public interface UserCommand {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User createUser( User user ) throws ParseException;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    User updateUser( User user ) throws ParseException;

    @PreAuthorize("hasRole('ROLE_USER')")
    void updatePassword(String uuid, Password password);

    UserEntity registerUser(User user ) throws ParseException;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteUser( String uuid );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Boolean restoreUser( String uuid );

}