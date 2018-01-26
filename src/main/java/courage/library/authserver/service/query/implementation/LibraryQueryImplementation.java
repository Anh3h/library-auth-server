package courage.library.authserver.service.query.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dto.Library;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.service.mapper.LibraryMapper;
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

    @Override
    public Library findLibraryById(String uuid) {
        LibraryEntity libraryEntity = libraryRepository.findByUuidAndEnabled(uuid, true);
        return LibraryMapper.getLibraryDTO(libraryEntity);
    }

    @Override
    public Page<Library> findLibraries(Integer pageNumber, Integer pageSize) {
        Page<LibraryEntity> libraryEntities = libraryRepository.findByEnabled(new PageRequest(pageNumber-1, pageSize), true);
        return libraryEntities.map(libraryEntity -> LibraryMapper.getLibraryDTO(libraryEntity));
    }

}
