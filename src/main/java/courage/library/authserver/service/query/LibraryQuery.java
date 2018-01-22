package courage.library.authserver.service.query;

import courage.library.authserver.dto.Library;
import org.springframework.data.domain.Page;

public interface LibraryQuery {

    Library findLibraryById(String uuid);
    Page<Library> findLibraries(Integer pageNumber, Integer pageSize );

}
