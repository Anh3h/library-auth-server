package courage.library.authserver.service.query.implementation;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.repository.RoleRepository;
import courage.library.authserver.service.mapper.RoleMappper;
import courage.library.authserver.service.query.RoleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleQueryImplementation implements RoleQuery {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findRoleById(String id) {
        RoleEntity roleEntity = roleRepository.findByIdAndEnabled(id, true);
        return RoleMappper.getRoleDTO(roleEntity);
    }

    @Override
    public Page<Role> findRoles(Integer pageNumber, Integer pageSize) {
        Page<RoleEntity> entities = roleRepository.findByEnabled(new PageRequest(pageNumber-1, pageSize), true);
        return entities.map(roleEntity -> RoleMappper.getRoleDTO(roleEntity));
    }

}
