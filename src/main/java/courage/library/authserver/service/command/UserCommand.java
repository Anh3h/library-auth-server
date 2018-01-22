package courage.library.authserver.service.command;

import courage.library.authserver.dto.User;

public interface UserCommand {

    User createUser( User user );
    User updateUser( User user );
    void deleteUser( String uuid );

}