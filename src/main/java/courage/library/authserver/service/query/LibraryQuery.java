package courage.library.authserver.service.query;

import courage.library.authserver.dto.Library;
import courage.library.authserver.dto.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface LibraryQuery {

    Library findLibraryById(String uuid);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Library findAllLibraryById(String uuid);

    Page<Library> findLibraries( Integer pageNumber, Integer pageSize );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<Library> findAllLibraries( Integer pageNumber, Integer pageSize );

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
    Page<User> findLibrarians(String libaryId, Integer pageNumber, Integer pageSize);

}
