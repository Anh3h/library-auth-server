package courage.library.authserver.service.command;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Password;

import java.text.ParseException;

public interface VerificationTokenCommand {

    void createVerificationToken(UserEntity user, String token, String tokenType) throws ParseException;
    void confirmRegisteredUserAccount(String VerificationToken);
    void changeForgottenPassword(String verificationToken, Password password);

}
