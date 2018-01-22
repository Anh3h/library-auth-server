package courage.library.authserver.service.command;

import courage.library.authserver.dto.Library;

public interface LibraryCommand {

    Library createLibrary( Library library );
    Library updateLibrary( Library library );
    void deleteLibrary( String uuid );

}
