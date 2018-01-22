package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dto.Library;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.jdbcTemplate.LibraryJdbcTemplate;
import courage.library.authserver.service.command.LibraryCommand;
import courage.library.authserver.service.mapper.LibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LibraryCommandImplementation implements LibraryCommand {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryJdbcTemplate libraryJdbcTemplate;

    @Override
    public Library createLibrary(Library library) {
        if ( libraryRepository.findByName(library.getName()) == null ) {
            library.setUuid(UUID.randomUUID().toString());
            LibraryEntity libraryEntity = libraryRepository.save(LibraryMapper.getLibraryDAO(library));

            return LibraryMapper.getLibraryDTO(libraryEntity);
        }
        throw ConflictException.create("Conflict: Library, {0} already exist", library.getName());
    }

    @Override
    public Library updateLibrary(Library library) {
        if (library.getUuid() != null) {
            return libraryJdbcTemplate.updateEntity(LibraryMapper.getLibraryDAO(library));
        }
        throw BadRequestException.create("Bad Request: Library uuid cannot be null");
    }

    @Override
    public void deleteLibrary(String uuid) {

        if ( libraryRepository.findByUuidAndIsAvailable(uuid, true).getUsers().isEmpty() ) {
            libraryJdbcTemplate.deleteEntity(uuid);
        }
        throw BadRequestException.create("Bad Request: Library has one/more librarians");
    }

}
