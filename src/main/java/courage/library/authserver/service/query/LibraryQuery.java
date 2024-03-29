package courage.library.authserver.service.query;

import courage.library.authserver.dto.Library;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
public interface LibraryQuery {

    Library findLibraryById(String uuid);
    Page<Library> findLibraries(Integer pageNumber, Integer pageSize );

}
