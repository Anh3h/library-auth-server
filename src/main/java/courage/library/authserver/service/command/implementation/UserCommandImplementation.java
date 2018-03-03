package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.LibraryEntity;
import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.User;
import courage.library.authserver.dto.Message.UserMessage;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.LibraryRepository;
import courage.library.authserver.repository.RoleRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.repository.jdbcTemplate.RoleJdbcTemplate;
import courage.library.authserver.repository.jdbcTemplate.UserJdbcTemplate;
import courage.library.authserver.service.AsyncNotifcation.MessageSender;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserCommandImplementation implements UserCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJdbcTemplate userJdbcTemplate;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private RoleJdbcTemplate roleJdbcTemplate;

    @Override
    @Transactional
    public User createUser(User user) {
        if (user.getEmail().isEmpty()) {
            throw BadRequestException.create("User email cannot be empty");
        }
        if ( userRepository.findByEmail(user. getEmail()) == null ) {

            user.setUuid(UUID.randomUUID().toString());
            UserEntity userEntity = UserMapper.getUserDAO(user);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity = this.setUserLibraryDetails(userEntity);
            userEntity = this.setRolesDetails(userEntity);

            User newUser = UserMapper.getUserDTO( userRepository.save(userEntity) );
            UserMessage userMessage = UserMapper.getUserMessage(newUser);
            userMessage.setAction("create");
            messageSender.broadcastMessage(userMessage);
            return newUser;
        }
        throw ConflictException.create("Conflict: User, {0} already exist", user.getEmail());
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        if (user.getUuid() != null){
            if (user.getEmail().isEmpty()) {
                throw BadRequestException.create("User email cannot be empty");
            }
            UserEntity entity = userRepository.findByUuidAndAccountLocked(user.getUuid(), false);
            if (entity != null) {
                Integer userId = entity.getId();
                user.setPassword(entity.getPassword());
                UserEntity userEntity = UserMapper.getUserDAO(user);
                userEntity.setId(userId);
                userEntity.setEmail(entity.getEmail());
                userEntity = this.setUserLibraryDetails(userEntity);
                userEntity = this.setRolesDetails(userEntity);

                User updatedUser = UserMapper.getUserDTO( userRepository.save(userEntity) );
                UserMessage userMessage = UserMapper.getUserMessage(updatedUser);
                userMessage.setAction("update");
                messageSender.broadcastMessage(userMessage);
                return updatedUser;
            }
            throw NotFoundException.create("Not found: user with uuid, {0} does not exist", user.getUuid());
        }
        throw BadRequestException.create("Bad Request: User uuid cannot be null");
    }

    @Override
    @Transactional
    public void updatePassword(String uuid, Password password) {
        UserEntity user = userRepository.findByUuidAndAccountLocked(uuid, false);
        if ( user != null ) {
            if (passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                userRepository.save(user);
            } else {
                throw BadRequestException.create("Bad Request: Invalid user password");
            }
            return;
        }
        throw NotFoundException.create("Not found: user with uuid, {0} does not exist", uuid);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteUser(String uuid) {
        if ( userRepository.findByUuid(uuid) != null) {
            userJdbcTemplate.deleteEntity(uuid);
            User user = UserMapper.getUserDTO( userRepository.findByUuid(uuid) );
            UserMessage userMessage = UserMapper.getUserMessage(user);
            userMessage.setAction("delete");
            messageSender.broadcastMessage(userMessage);
        }
    }

    @Override
    @Transactional
    public Boolean restoreUser(String uuid) {
        if ( userRepository.findByUuid(uuid) != null){
            userJdbcTemplate.restoreEntity(uuid);
            User user = UserMapper.getUserDTO( userRepository.findByUuid(uuid) );
            UserMessage userMessage = UserMapper.getUserMessage(user);
            userMessage.setAction("restore");
            messageSender.broadcastMessage(userMessage);
            return true;
        }
        throw NotFoundException.create("Not found: user with uuid, {0} does not exist", uuid);
    }

    private UserEntity setUserLibraryDetails(UserEntity userEntity) {
        if (userEntity.getLibrary() != null) {
            if (userEntity.getLibrary().getUuid() != null) {
                LibraryEntity libraryEntity = libraryRepository.findByUuidAndEnabled
                        (userEntity.getLibrary().getUuid(), true);
                if (libraryEntity != null){
                    userEntity.setLibrary(libraryEntity);
                    RoleEntity role = roleJdbcTemplate.findByName("ROLE_LIBRARIAN");
                    if (role != null ) {
                        userEntity.addRole(role);
                    }
                    return userEntity;
                }
                throw NotFoundException.
                        create("Library with uuid {0} does not exist", userEntity.getLibrary().getUuid());
            }
            throw BadRequestException.create("Library uuid cannot be empty");
        }
        userEntity.setLibrary(null);
        return userEntity;
    }

    private UserEntity setRolesDetails(UserEntity userEntity) {
        List<RoleEntity> roles = new ArrayList<>();
        if (userEntity.getRoles() != null || userEntity.getRoles().size() > 0) {
            userEntity.getRoles().forEach(roleEntity -> {
                if (roleEntity.getId() != null) {
                    RoleEntity role = roleRepository.findById(roleEntity.getId());
                    if (role == null) {
                        throw NotFoundException.create("Not Found: Role with id {0} does not exist", roleEntity.getId());
                    }
                    if (role.getName().compareTo("ROLE_LIBRARIAN") == 0 && userEntity.getLibrary() == null) {
                        throw BadRequestException.create("Bad Request: Librarian must have library");
                    }
                    roles.add(role);
                    return;
                }
                throw BadRequestException.create("Bad Request: Role id cannot be null");
            });
            userEntity.setRoles(roles);
        }
        return userEntity;
    }

}
