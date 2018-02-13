package courage.library.authserver.eventandlistner;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.User;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.command.VerificationTokenCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenCommand  tokenCommand;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            UserEntity user = event.getUser();
            String token = UUID.randomUUID().toString();
            tokenCommand.createVerificationToken(user, token, event.getEventType());
            if (event.getEventType().equalsIgnoreCase("emailVerification")) {
                this.confirmRegistrationEvent(token, user);
            } else if (event.getEventType().equalsIgnoreCase("forgotPassword")) {
                this.forgotPasswordEvent(token, user);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void forgotPasswordEvent(String token, UserEntity user) {

        String email = user.getEmail();
        String subject = "Password Reset";
        String confirmationUrl = "http://localhost:8081/resetPassword?token="+token;
        String message = "Hi " + user.getFirstName() + ", Looks like you forgot the password to your. " +
                "Alles account. Please click the following link to reset you password: " + confirmationUrl;

        sendMail(email, subject, message);

    }

    private void confirmRegistrationEvent(String token, UserEntity  user) {

        String email = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:8081/confirmRegistration?token="+token;
        String message = "Hi " + user.getFirstName() + ", This message is to confirm" +
                "your Alles account.\n Verifying your email address helps you secure your " +
                "account. If you forgot your password, you will now be able to reset it by email." +
                "To confirm that this is your Alles account, click here: " + confirmationUrl;

        sendMail(email, subject, message);


    }

    private void sendMail(String email, String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("no_reply@alles.com");
        mail.setSubject(subject);
        mail.setText(message);

        try {
            mailSender.send(mail);
            System.out.println("Mail sent");
        } catch (MailException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
