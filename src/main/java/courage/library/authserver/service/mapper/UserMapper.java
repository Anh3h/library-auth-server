package courage.library.authserver.service.mapper;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.dto.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User getUserDTO( UserEntity userEntity ) {

        if (userEntity == null) {
            return null;
        }
        List<Role> roles  = new ArrayList<>();
        userEntity.getRoles().forEach( roleEntity -> {
            roles.add(RoleMappper.getRoleDTO(roleEntity));
        } );
        return new User( userEntity.getUuid(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getEmail(), userEntity.getPassword(), userEntity.getDob(), userEntity.getTelephone(),
                userEntity.getAddress(), LibraryMapper.getLibraryDTO(userEntity.getLibrary()), roles );
    }

    public static UserEntity getUserDAO( User user ) {

        if (user == null) {
            return null;
        }

        List<RoleEntity> roleEntities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            roleEntities.add(RoleMappper.getRoleDAO(role));
        });
        return new UserEntity(user.getUuid(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getDob(), user.getTelephone(), user.getAddress(),
                LibraryMapper.getLibraryDAO(user.getLibrary()), true, roleEntities);
    }

}
