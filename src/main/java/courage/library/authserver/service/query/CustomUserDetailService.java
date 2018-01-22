package courage.library.authserver.service.query;

import courage.library.authserver.dto.Role;
import courage.library.authserver.dto.User;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.service.mapper.RoleMappper;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserMapper.getUserDTO( this.userRepository.findByEmailAndIsAvailable(email, true) );
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Email[%s] not found", email));
        }

        return  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                true, true, true, true, user.getRoles());
    }

}
