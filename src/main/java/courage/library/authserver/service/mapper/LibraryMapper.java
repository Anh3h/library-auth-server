package courage.library.authserver.service.mapper;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dto.Library;

public class LibraryMapper {

    public static Library getLibraryDTO( LibraryEntity libraryEntity ) {
        if (libraryEntity == null){
            return  null;
        }
        return new Library(libraryEntity.getUuid(), libraryEntity.getName(),
                libraryEntity.getAddress(), libraryEntity.getLogo());
    }

    public static LibraryEntity getLibraryDAO( Library library ) {
        if (library == null){
            return  null;
        }
        return new LibraryEntity(library.getUuid(), library.getName(), library.getAddress(),
                library.getLogo(), true);
    }
}
