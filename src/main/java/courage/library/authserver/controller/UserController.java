package courage.library.authserver.controller;

import courage.library.authserver.dto.User;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.command.UserCommand;
import courage.library.authserver.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserQuery userQuery;

    @Autowired
    private UserCommand userCommand;

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> createUser( @RequestBody User user ) {
        User newUser = this.userCommand.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size ){
        if( page == null || size == null ) {
            page = 1;
            size = 20;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<User> users = this.userQuery.findUsers(page, size);
        if (page > users.getTotalPages()) {
            throw NotFoundException.create("Page number does not exist");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUser( @PathVariable("userId")String userId ) {
        User user = this.userQuery.findUserById(userId);
        if (user == null) {
            throw NotFoundException.create("User with id, {0} does not exist", userId);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> updateUser( @RequestBody User user,
                                            @PathVariable("userId")String userId) {
        if ( this.userQuery.findUserById(userId) == null ) {
            throw NotFoundException.create("User with id, {0} does not exist", userId);
        }
        User updateUser = userCommand.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<HttpStatus> deleteUser( @PathVariable("userId")String userId ) {
        userCommand.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
