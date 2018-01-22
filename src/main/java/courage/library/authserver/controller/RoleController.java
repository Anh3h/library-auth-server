package courage.library.authserver.controller;

import courage.library.authserver.dto.Role;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.command.RoleCommand;
import courage.library.authserver.service.query.RoleQuery;
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

    @Autowired
    private RoleCommand roleCommand;

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Role> createRole( @RequestBody Role role ) {
        System.out.println("Creating new role");
        Role newRole = this.roleCommand.createRole( role );
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

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
            throw BadRequestException.create("Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<Role> roles = this.roleQuery.findRoles(page, size);
        if (page > roles.getTotalPages()) {
            throw NotFoundException.create("Page number does not exist");
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{roleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Role> getRole( @PathVariable("roleId")String roleId ) {
        Role role = this.roleQuery.findRoleById(roleId);
        if (role == null) {
            throw NotFoundException.create("Role with id, {0} does not exist", roleId);
        }
        return new ResponseEntity<>( role, HttpStatus.OK );
    }

    @RequestMapping(
            value = "/{roleId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Role> updateRole( @RequestBody Role role,
                                            @PathVariable("roleId")String roleId ) {
        if (this.roleQuery.findRoleById(roleId) == null) {
            throw NotFoundException.create("Role with id, {0} does not exist", roleId);
        }
        Role updatedRole = this.roleCommand.updateRole(role);
        return new ResponseEntity<>( updatedRole, HttpStatus.OK );
    }

    @RequestMapping(
            value = "/{roleId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<HttpStatus> deleteRole( @PathVariable("roleId")String roleId ) {
        this.roleCommand.deleteRole(roleId);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }

}
