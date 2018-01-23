package courage.library.authserver.service.query;

import courage.library.authserver.dto.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface UserQuery {

    @PreAuthorize("hasRole('ROLE_USER')")
    User findUserById(String uuid);

    Page<User> findUsers(Integer pageNumber, Integer pageSize );

}
