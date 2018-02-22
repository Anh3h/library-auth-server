package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dto.Message.LibraryMessage;
import courage.library.authserver.service.AsyncNotifcation.MessageSender;
import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Library;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.jdbcTemplate.LibraryJdbcTemplate;
import courage.library.authserver.service.command.LibraryCommand;
import courage.library.authserver.service.mapper.LibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LibraryCommandImplementation implements LibraryCommand {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryJdbcTemplate libraryJdbcTemplate;

    @Autowired
    private MessageSender messageSender;

    @Override
    public Library createLibrary(Library library) {
        if ( libraryRepository.findByName(library.getName()) == null ) {
            library.setUuid(UUID.randomUUID().toString());
            LibraryEntity libraryEntity = libraryRepository.save(LibraryMapper.getLibraryDAO(library));

            Library newLibrary = LibraryMapper.getLibraryDTO(libraryEntity);
            LibraryMessage libraryMessage = LibraryMapper.getLibraryMessage(newLibrary);
            libraryMessage.setAction("create");
            messageSender.broadcastMessage(libraryMessage);
            return newLibrary;
        }
        throw ConflictException.create("Conflict: Library, {0} already exist", library.getName());
    }

    @Override
    public Library updateLibrary(Library library) {
        if (library.getUuid() != null) {
            if ( this.libraryRepository.findByUuidAndEnabled(library.getUuid(), true) != null) {
                Library updatedLibrary = libraryJdbcTemplate.updateEntity(LibraryMapper.getLibraryDAO(library));
                LibraryMessage libraryMessage = LibraryMapper.getLibraryMessage(updatedLibrary);
                libraryMessage.setAction("update");
                messageSender.broadcastMessage(libraryMessage);
                return updatedLibrary;
            }
            throw NotFoundException.create("Not Found: Library with uuid, {0} does not exist", library.getUuid());
        }
        throw BadRequestException.create("Bad Request: Library uuid cannot be null");
    }

    @Override
    public void deleteLibrary(String uuid) {
        LibraryEntity libraryEntity = libraryRepository.findByUuid(uuid);
        if (libraryEntity != null) {
            List<UserEntity> users = libraryEntity.getUsers();
            if (users == null || users.isEmpty()) {
                libraryJdbcTemplate.deleteEntity(uuid);
                Library library = LibraryMapper.getLibraryDTO( libraryRepository.findByUuid(uuid) );
                LibraryMessage libraryMessage = LibraryMapper.getLibraryMessage(library);
                libraryMessage.setAction("delete");
                messageSender.broadcastMessage(libraryMessage);
            }
            throw BadRequestException.create("Bad Request: Library has one/more librarians");
        }
    }

    @Override
    public Boolean restoreLibrary(String uuid) {
        if ( libraryRepository.findByUuid(uuid) != null) {
            libraryJdbcTemplate.restoreEntity(uuid);
            Library library = LibraryMapper.getLibraryDTO( libraryRepository.findByUuid(uuid) );
            LibraryMessage libraryMessage = LibraryMapper.getLibraryMessage(library);
            libraryMessage.setAction("restore");
            messageSender.broadcastMessage(libraryMessage);
            return true;
        }
        throw BadRequestException.create("Bad Request: Library with uuid, {0} does not exist", uuid);
    }

}
