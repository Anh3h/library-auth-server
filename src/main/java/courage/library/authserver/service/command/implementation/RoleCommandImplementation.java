package courage.library.authserver.service.command.implementation;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.ConflictException;
import courage.library.authserver.repository.RoleRepository;
import courage.library.authserver.repository.jdbcTemplate.RoleJdbcTemplate;
import courage.library.authserver.service.command.RoleCommand;
import courage.library.authserver.service.mapper.RoleMappper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RoleCommandImplementation implements RoleCommand {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleJdbcTemplate roleJdbcTemplate;

    @Override
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()) == null) {
            role.setId(UUID.randomUUID().toString());
            RoleEntity roleEntity = roleRepository.save(RoleMappper.getRoleDAO(role));
            return RoleMappper.getRoleDTO(roleEntity);
        }
        throw  ConflictException.create("Conflict: Role, {0} already exist", role.getName());
    }

    @Override
    public Role updateRole(Role role) {
        RoleEntity roleEntity = roleRepository.save(RoleMappper.getRoleDAO(role));
        return RoleMappper.getRoleDTO(roleEntity);
    }

    @Override
    public void deleteRole(String id) {
        if ( roleRepository.findByIdAndIsAvailable(id, true).getUsers() == null ) {
            roleJdbcTemplate.deleteEntity(id);
        }
        throw BadRequestException.create("Bad Request: Role is used by one/more user ");
    }

}
