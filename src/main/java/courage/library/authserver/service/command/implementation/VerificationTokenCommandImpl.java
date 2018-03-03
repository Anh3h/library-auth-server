package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dao.VerificationTokenEntity;
import courage.library.authserver.dto.ForgotPassword;
import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.User;
import courage.library.authserver.dto.Message.UserMessage;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ForbiddenException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.repository.RoleRepository;
import courage.library.authserver.repository.TokenRepository;
import courage.library.authserver.repository.UserRepository;
import courage.library.authserver.repository.jdbcTemplate.RoleJdbcTemplate;
import courage.library.authserver.service.AsyncNotifcation.MessageSender;
import courage.library.authserver.service.command.VerificationTokenCommand;
import courage.library.authserver.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Transactional
public class VerificationTokenCommandImpl implements VerificationTokenCommand {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RoleJdbcTemplate roleJdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSender messageSender;

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
                RoleEntity role = roleJdbcTemplate.findByName("ROLE_USER");
                if (role != null ) {
                    userEntity.addRole(role);
                }
                userEntity.setEnabled(true);
                User user = UserMapper.getUserDTO( userRepository.save(userEntity) );
                UserMessage userMessage = UserMapper.getUserMessage(user);
                userMessage.setAction("create");
                messageSender.broadcastMessage(userMessage);

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
    public void changeForgottenPassword(String verificationToken, ForgotPassword password) {
        VerificationTokenEntity tokenEntity = tokenRepository.findByToken(verificationToken);
        if (tokenEntity != null) {
            Long timeLeft = tokenEntity.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime();
            if (timeLeft > 0 || tokenEntity.getVerified() == false) {
                if (password.getNewPassword().compareTo(password.getRepeatNewPassword()) == 0) {
                    UserEntity user = tokenEntity.getUser();
                    user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                    userRepository.save(user);

                    tokenEntity.setVerified(true);
                    tokenRepository.save(tokenEntity);
                    return;
                }
                throw BadRequestException.create("Bad Request: Two passwords must the same");
            }
            throw ForbiddenException.create("Forbidden: Token expired");
        }
        throw NotFoundException.create("Not found: verification token, {0} does not exist", verificationToken);
    }

}
