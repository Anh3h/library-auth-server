package courage.library.authserver.service.query;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserQuery {

    User findUserById(String uuid);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User findAllUserById(String uuid);

    UserEntity findUserByEmail(String email);

    Page<User> findUsers(Integer pageNumber, Integer pageSize );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<User> findAllUsers(Integer pageNumber, Integer pageSize );

}
