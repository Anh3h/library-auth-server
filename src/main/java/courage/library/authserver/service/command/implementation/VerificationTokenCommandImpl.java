package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dao.VerificationTokenEntity;
import courage.library.authserver.dto.Password;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ForbiddenException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.RoleRepository;
import courage.library.authserver.repository.TokenRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.service.command.VerificationTokenCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class VerificationTokenCommandImpl implements VerificationTokenCommand {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createVerificationToken(UserEntity user, String token, String tokenType) {
        VerificationTokenEntity tokenEntity = new VerificationTokenEntity(token, user, tokenType);
        tokenRepository.save(tokenEntity);
    }

    @Override
    public void confirmRegisteredUserAccount(String verificationToken) {
        VerificationTokenEntity tokenEntity = tokenRepository.findByToken(verificationToken);
        if (tokenEntity != null) {
            Long timeLeft = tokenEntity.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime();
            if (timeLeft > 0 || tokenEntity.getVerified() == false) {
                UserEntity userEntity = tokenEntity.getUser();
                userEntity.addRole(roleRepository.findByName("ROLE_USER"));
                userEntity.setEnabled(true);
                userRepository.save(userEntity);

                tokenEntity.setVerified(true);
                tokenRepository.save(tokenEntity);
            } else {
                throw ForbiddenException.create("Forbidden: Token expired");
            }
            return;
        }
        throw NotFoundException.create("Not found: verification token, {0} does not exist", verificationToken);
    }

    @Override
    public void changeForgottenPassword(String verificationToken, Password password) {
        VerificationTokenEntity tokenEntity = tokenRepository.findByToken(verificationToken);
        if (tokenEntity == null) {
            Long timeLeft = tokenEntity.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime();
            if (timeLeft > 0 || tokenEntity.getVerified() == false) {
                UserEntity user = tokenEntity.getUser();
                user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                userRepository.save(user);

                tokenEntity.setVerified(true);
                tokenRepository.save(tokenEntity);
            } else {
                throw ForbiddenException.create("Forbidden: Token expired");
            }
            return;
        }
        throw NotFoundException.create("Not found: verification token, {0} does not exist", verificationToken);
    }

}
