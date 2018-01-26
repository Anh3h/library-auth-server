package courage.library.authserver.controller;

import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.GenericReponse;
import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.SimpleUser;
import courage.library.authserver.dto.User;
import courage.library.authserver.eventandlistner.OnRegistrationCompleteEvent;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.command.VerificationTokenCommand;
import courage.library.authserver.service.query.UserQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserCommand userCommand;

    @Autowired
    private VerificationTokenCommand tokenCommand;

    @Autowired
    private UserQuery userQuery;

    @ApiOperation(value="Register new user account")
    @RequestMapping(
            value = "/registration",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> registerUserAccount( @RequestBody SimpleUser user ) throws ParseException {
        User userDTO = new User(null, user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getDob(), user.getTelephone(),
                user.getAddress());
        UserEntity registeredUser = userCommand.registerUser(userDTO);

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, "emailVerification"));
        } catch (Exception ex){

        }

        Map<String, String> response = new HashMap<>();
        response.put("response", "A mail has been sent to your email account, " +
                "please confirm it to activate your account");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="Confirm user registered email")
    @RequestMapping(
            value = "/confirmRegistration",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> confirmRegistration( @RequestParam("token") String token ) {
        tokenCommand.confirmRegisteredUserAccount(token);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Registration confirm, account activated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value="Forgot password")
    @RequestMapping(
            value = "/forgotPassword",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam("email") String email) {
        UserEntity user = userQuery.findUserByEmail(email);
        if (user == null) {
            throw NotFoundException.create("User with email {0} does not exist", email);
        }
        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, "forgotPassword"));
        } catch (Exception ex){

        }

        Map<String, String> response = new HashMap<>();
        response.put("response", "A mail has been sent to your email account, " +
                "please confirm it to reset your password");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value="Update password after forgot password")
    @RequestMapping(
            value = "/resetPassword",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Password password,
                                                        @RequestParam("token") String token) {
        tokenCommand.changeForgottenPassword(token, password);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Successfully updated password");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

