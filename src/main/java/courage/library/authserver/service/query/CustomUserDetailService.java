package courage.library.authserver.service.query;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ForbiddenException;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = this.userRepository.findByEmailAndAccountLocked(email, false);

            if (userEntity == null) {
                throw new UsernameNotFoundException(String.format("Email[%s] not found", email));
            }else if (userEntity.getEnabled() == false) {
                throw ForbiddenException.create("Forbidden: User has to confirm before using this account");
            }

            User user = UserMapper.getUserDTO(userEntity);
            return  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    userEntity.getEnabled(), true, true, true, user.getRoles());
        } catch (RuntimeException ex) {
            throw BadRequestException.create(ex.getMessage());
        }

    }

}
