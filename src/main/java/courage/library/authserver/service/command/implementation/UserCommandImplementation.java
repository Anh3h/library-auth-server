package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.repository.jdbcTemplate.UserJdbcTemplate;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserCommandImplementation implements UserCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJdbcTemplate userJdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public User createUser(User user) {
        if ( userRepository.findByEmail(user. getEmail()) == null ) {
            user.setUuid(UUID.randomUUID().toString());
            UserEntity userEntity = UserMapper.getUserDAO(user);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

            if (userEntity.getLibrary().getUuid() != null) {
                Integer libraryId = this.getLibraryId(userEntity);
                userEntity.getLibrary().setId(libraryId);
            }

            UserEntity newUserEntity = userRepository.save(userEntity);

            return UserMapper.getUserDTO(newUserEntity);
        }
        throw ConflictException.create("Conflict: User, {0} already exist", user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        if (user.getUuid() != null){
            Integer userId = userRepository.findByUuidAndIsAvailable(user.getUuid(), true).getId();
            if (userId == null) {
                throw NotFoundException.create("Not found: user with uuid, {0} does not exist", user.getUuid());
            }
            UserEntity userEntity = UserMapper.getUserDAO(user);
            userEntity.setId(userId);

            if (userEntity.getLibrary().getUuid() != null) {
                Integer libraryId = this.getLibraryId(userEntity);
                userEntity.getLibrary().setId(libraryId);
            }

            UserEntity updatedUserEntity = userRepository.save(userEntity);
            return UserMapper.getUserDTO(updatedUserEntity);

        }
        throw BadRequestException.create("Bad Request: User uuid cannot be null");
    }

    @Override
    public void deleteUser(String uuid) {
        userJdbcTemplate.deleteEntity(uuid);
    }

    private Integer getLibraryId(UserEntity userEntity) {
        LibraryEntity libraryEntity = libraryRepository.findByUuidAndIsAvailable
                (userEntity.getLibrary().getUuid(), true);
        return libraryEntity.getId();
    }

}
