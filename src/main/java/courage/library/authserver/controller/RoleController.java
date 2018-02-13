package courage.library.authserver.controller;

import courage.library.authserver.dto.Role;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.query.RoleQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleQuery roleQuery;

    @ApiOperation(value="Get all/some roles")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<Role>> getRoles(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size ) {
        if( page == null || size == null ) {
            page = 1;
            size = 20;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Bad Request:  Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<Role> roles = this.roleQuery.findRoles(page, size);
        if (page > roles.getTotalPages()) {
            throw NotFoundException.create("Not Found: Page number does not exist");
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @ApiOperation(value="Get a role based on role_id")
    @RequestMapping(
            value = "/{roleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Role> getRole( @PathVariable("roleId")String roleId ) {
        Role role = this.roleQuery.findRoleById(roleId);
        if (role == null) {
            throw NotFoundException.create("Not Found: Role with id, {0} does not exist", roleId);
        }
        return new ResponseEntity<>( role, HttpStatus.OK );
    }

}
