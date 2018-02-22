package courage.library.authserver.controller;

import java.util.Map;

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
        Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
        page = pageAttributes.get("page");
        size = pageAttributes.get("size");

        Page<Role> roles = this.roleQuery.findRoles(page, size);
        if (page > roles.getTotalPages()) {
            throw NotFoundException.create("Not Found: Empty page");
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
