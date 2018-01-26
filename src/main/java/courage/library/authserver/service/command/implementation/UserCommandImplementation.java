package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dao.VerificationTokenEntity;
import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.User;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.exception.ForbiddenException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.TokenRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.repository.jdbcTemplate.UserJdbcTemplate;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
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
            userEntity = this.setUserLibraryDetails(userEntity);

            UserEntity newUserEntity = userRepository.save(userEntity);

            return UserMapper.getUserDTO(newUserEntity);
        }
        throw ConflictException.create("Conflict: User, {0} already exist", user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        if (user.getUuid() != null){
            Integer userId = userRepository.findByUuidAndAccountLocked(user.getUuid(), false).getId();
            if (userId != null) {

                UserEntity userEntity = UserMapper.getUserDAO(user);
                userEntity.setId(userId);
                userEntity = this.setUserLibraryDetails(userEntity);

                UserEntity updatedUserEntity = userRepository.save(userEntity);
                return UserMapper.getUserDTO(updatedUserEntity);
            }
            throw NotFoundException.create("Not found: user with uuid, {0} does not exist", user.getUuid());
        }
        throw BadRequestException.create("Bad Request: User uuid cannot be null");
    }

    @Override
    public void updatePassword(String uuid, Password password) {
        UserEntity user = userRepository.findByUuidAndAccountLocked(uuid, false);
        if (passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password.getNewPassword()));
            userRepository.save(user);
        } else {
            throw BadRequestException.create("Invalid user password");
        }
    }

    @Override
    public UserEntity registerUser(User user) {
        if ( userRepository.findByEmail(user. getEmail()) == null ) {
            user.setUuid(UUID.randomUUID().toString());
            UserEntity userEntity = UserMapper.getUserDAO(user);
            userEntity.setEnabled(false);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity newUserEntity = userRepository.save(userEntity);

            return newUserEntity;
        }
        throw ConflictException.create("Conflict: User, {0} already exist", user.getEmail());

    }

    @Override
    public void deleteUser(String uuid) {
        userJdbcTemplate.deleteEntity(uuid);
    }

    private Integer getLibraryId(UserEntity userEntity) {
        LibraryEntity libraryEntity = libraryRepository.findByUuidAndEnabled
                (userEntity.getLibrary().getUuid(), true);
        return libraryEntity.getId();
    }

    private UserEntity setUserLibraryDetails(UserEntity userEntity) {
        if (userEntity.getLibrary() != null && userEntity.getLibrary().getUuid() != null) {
            Integer libraryId = this.getLibraryId(userEntity);
            userEntity.getLibrary().setId(libraryId);
        }
        return userEntity;
    }

}
