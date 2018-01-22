package courage.library.authserver.service.query;

import courage.library.authserver.dto.User;
import org.springframework.data.domain.Page;

public interface UserQuery {

    User findUserById(String uuid);
    Page<User> findUsers(Integer pageNumber, Integer pageSize );

}
