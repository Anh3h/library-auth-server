package courage.library.authserver.service.command;

import courage.library.authserver.dto.Library;
import org.springframework.security.access.prepost.PreAuthorize;

public interface LibraryCommand {

    Library createLibrary( Library library );

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
    Library updateLibrary( Library library );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteLibrary( String uuid );

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Boolean restoreLibrary( String uuid );

}
