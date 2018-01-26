package courage.library.authserver.service.query;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserQuery {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User findUserById(String uuid);

    UserEntity findUserByEmail(String email);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<User> findUsers(Integer pageNumber, Integer pageSize );

}
