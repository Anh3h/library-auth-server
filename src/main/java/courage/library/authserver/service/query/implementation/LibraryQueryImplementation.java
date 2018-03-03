package courage.library.authserver.service.query.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Library;
import courage.library.authserver.dto.User;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.service.mapper.LibraryMapper;
import courage.library.authserver.service.mapper.UserMapper;
import courage.library.authserver.service.query.LibraryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LibraryQueryImplementation implements LibraryQuery {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Library findLibraryById(String uuid) {
        LibraryEntity libraryEntity = libraryRepository.findByUuidAndEnabled(uuid, true);
        return LibraryMapper.getLibraryDTO(libraryEntity);
    }

    @Override
    public Library findAllLibraryById(String uuid) {
        LibraryEntity libraryEntity = libraryRepository.findByUuid(uuid);
        return LibraryMapper.getLibraryDTO(libraryEntity);
    }

    @Override
    public Page<Library> findLibraries(Integer pageNumber, Integer pageSize) {
        Page<LibraryEntity> libraryEntities = libraryRepository.findByEnabled(new PageRequest(pageNumber-1, pageSize), true);
        return libraryEntities.map(libraryEntity -> LibraryMapper.getLibraryDTO(libraryEntity));
    }

    @Override
    public Page<Library> findAllLibraries(Integer pageNumber, Integer pageSize) {
        Page<LibraryEntity> libraryEntities = libraryRepository.findAll(new PageRequest(pageNumber-1, pageSize));
        return libraryEntities.map(libraryEntity -> LibraryMapper.getLibraryDTO(libraryEntity));
    }

    @Override
    public Page<User> findLibrarians(String libraryId, Integer pageNumber, Integer pageSize) {
        LibraryEntity libraryEntity = libraryRepository.findByUuidAndEnabled(libraryId, true);
        if (libraryEntity != null) {
            Page<UserEntity> userEntities = userRepository.findByLibraryAndAccountLocked(libraryEntity,
                    new PageRequest(pageNumber - 1, pageSize),false);
            return userEntities.map(userEntity -> UserMapper.getUserDTO(userEntity));
        }
        throw NotFoundException.create("Not Found: Library with id, {0} does not exist", libraryId);
    }

}
