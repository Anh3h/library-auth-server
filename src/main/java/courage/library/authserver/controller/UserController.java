package courage.library.authserver.controller;

import courage.library.authserver.dto.Password;
import courage.library.authserver.dto.User;
import courage.library.authserver.dto.UserAccount;
import courage.library.authserver.eventandlistner.OnRegistrationCompleteEvent;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.query.UserQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserQuery userQuery;

    @Autowired
    private UserCommand userCommand;

    @ApiOperation(value="Create new user")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> createUser( @RequestBody UserAccount userAccount ) throws ParseException {
        System.out.println("creating new user");
        User user = new User(null, userAccount.getFirstName(), userAccount.getLastName(), userAccount.getEmail(),
                userAccount.getPassword(), userAccount.getDob(), userAccount.getTelephone(), userAccount.getAddress(),
                userAccount.getLibrary(), userAccount.getRoles());
        User newUser = this.userCommand.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @ApiOperation(value="Get all/some users")
    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size,
                                               @RequestParam(value =  "all", required = false) Boolean all ){
        if( page == null || size == null ) {
            page = 1;
            size = 20;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Bad Request: Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<User> users;
        if (all == null || all == false) {
            users = this.userQuery.findUsers(page, size);
        } else {
            users = this.userQuery.findAllUsers(page, size);
        }
        if (page > users.getTotalPages()) {
            throw NotFoundException.create("Not Found: Page number does not exist");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value="Get user based on user_id")
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUser( @PathVariable("userId")String userId,
                                         @RequestParam(value =  "all", required = false) Boolean all ) {
        User user;
        if (all == null || all == false) {
            user = this.userQuery.findUserById(userId);
        } else {
            user = this.userQuery.findAllUserById(userId);
        }

        if (user == null) {
            throw NotFoundException.create("Not Found: User with id, {0} does not exist", userId);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value="Update user based on user_id")
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> updateUser( @RequestBody User user,
                                            @PathVariable("userId")String userId) throws ParseException {
        if ( this.userQuery.findUserById(userId) == null ) {
            throw NotFoundException.create("Not Found: User with id, {0} does not exist", userId);
        }
        User updateUser = userCommand.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{userId}/updatePassword",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> updatePassword( @RequestBody Password password,
                                            @PathVariable("userId")String userId) throws ParseException {
        userCommand.updatePassword(userId, password);
        Map<String, String> response = new HashMap<>();
        response.put("response", "Successfully updated password");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value="Restore deleted user account")
    @RequestMapping(
            value = "/{userId}/activate",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> updateUser( @PathVariable("userId")String userId) {
        userCommand.restoreUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value="Delete user based on user_id")
    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<HttpStatus> deleteUser( @PathVariable("userId")String userId ) {
        userCommand.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

