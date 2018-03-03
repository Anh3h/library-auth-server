package courage.library.authserver.service.eventandlistner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.service.command.VerificationTokenCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.text.ParseException;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenCommand  tokenCommand;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

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

        sendMail(email, subject, getResetPwdMessage(user.getFirstName(), token));

    }

    private void confirmRegistrationEvent(String token, UserEntity  user) {

        String email = user.getEmail();
        String subject = "Registration Confirmation";

        sendMail(email, subject, getConfirmRegistrationMessage(user.getFirstName(),token ));

    }

    private String getConfirmRegistrationMessage(String name, String token) {
        final Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("token", token);
        ctx.setVariable("logo", new ClassPathResource("logo.png").getPath());
        return this.templateEngine.process(new ClassPathResource("confirmationMail").getPath(), ctx);
    }

    private String getResetPwdMessage(String name, String token) {
        final Context ctx = new Context();
        ctx.setVariable("name", name);
        ctx.setVariable("token", token);
        ctx.setVariable("logo", new ClassPathResource("logo.png").getPath());

        return this.templateEngine.process(new ClassPathResource("forgotPasswordMail").getPath(), ctx);
    }

    private void sendMail(String email, String subject, String message) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper mail;
        try {
            mail = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            mail.addAttachment("logo.png", new ClassPathResource("logo.png"));
            mail.setTo(email);
            mail.setFrom("no_reply@alles.com");
            mail.setSubject(subject);
            mail.setText(message, true);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }


        try {
            mailSender.send(mimeMessage);
            System.out.println("Mail sent");
        } catch (MailException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
