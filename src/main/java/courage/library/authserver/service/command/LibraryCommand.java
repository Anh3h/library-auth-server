package courage.library.authserver.service.command;

import courage.library.authserver.dto.Library;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_LIBRARIAN')")
public interface LibraryCommand {

    Library createLibrary( Library library );
    Library updateLibrary( Library library );
    void deleteLibrary( String uuid );

}
