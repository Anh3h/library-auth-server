package courage.library.authserver.service.mapper;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dto.Role;

public class RoleMappper {

    public static Role getRoleDTO( RoleEntity roleEntity ) {
        if (roleEntity == null) {
            return null;
        }
        return new Role(roleEntity.getId(), roleEntity.getName());
    }

    public static RoleEntity getRoleDAO( Role role ) {
        if (role == null) {
            return null;
        }
        return new RoleEntity(role.getId(), role.getName());
    }

}
