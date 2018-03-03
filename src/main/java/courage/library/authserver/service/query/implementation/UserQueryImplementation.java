package courage.library.authserver.service.query.implementation;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.service.mapper.UserMapper;
import courage.library.authserver.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserQueryImplementation implements UserQuery {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserById(String uuid) {
        UserEntity userEntity = userRepository.findByUuidAndAccountLocked(uuid, false);
        return UserMapper.getUserDTO(userEntity);
    }

    @Override
    public User findAllUserById(String uuid) {
        UserEntity userEntity = userRepository.findByUuid(uuid);
        return UserMapper.getUserDTO(userEntity);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmailAndAccountLockedAndEnabled(email, false, true);
        return userEntity;
    }

    @Override
    public Page<User> findUsers(Integer pageNumber, Integer pageSize) {
        Page<UserEntity> userEntities = userRepository.
                findByAccountLocked(new PageRequest(pageNumber-1, pageSize), false);
        return userEntities.map( userEntity -> UserMapper.getUserDTO(userEntity));
    }

    @Override
    public Page<User> findAllUsers(Integer pageNumber, Integer pageSize) {
        Page<UserEntity> userEntities = userRepository.findAll(new PageRequest(pageNumber-1, pageSize));
        return userEntities.map( userEntity -> UserMapper.getUserDTO(userEntity));
    }

}
