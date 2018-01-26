package courage.library.authserver.service.mapper;

import courage.library.authserver.dao.RoleEntity;
import courage.library.authserver.dao.UserEntity;
import courage.library.authserver.dto.Role;
import courage.library.authserver.dto.User;
import courage.library.authserver.exception.BadRequestException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserMapper {

    public static User getUserDTO( UserEntity userEntity ) {

        if (userEntity != null) {
            String dob;
            if (userEntity.getDob() == null) {
                dob = null;
            } else {
                dob = userEntity.getDob().toString();
            }

            List<Role> roles = new ArrayList<>();
            userEntity.getRoles().forEach(roleEntity -> {
                roles.add(RoleMappper.getRoleDTO(roleEntity));
            });

            return new User(userEntity.getUuid(), userEntity.getFirstName(), userEntity.getLastName(),
                    userEntity.getEmail(), userEntity.getPassword(), dob, userEntity.getTelephone(),
                    userEntity.getAddress(), LibraryMapper.getLibraryDTO(userEntity.getLibrary()), roles);
        }
        return null;
    }

    public static UserEntity getUserDAO( User user ) {

        if (user != null) {
            Date dob;
            if (user.getDob() == null || user.getDob().isEmpty()) {
                dob = null;
            } else {
                try {
                    dob = stringToDateObject(user.getDob());
                } catch (ParseException ex) {
                    throw BadRequestException.create("Invalid date format");
                }
            }

            List<RoleEntity> roleEntities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                roleEntities.add(RoleMappper.getRoleDAO(role));
            });

            return new UserEntity(user.getUuid(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getPassword(), dob, user.getTelephone(), user.getAddress(),
                    LibraryMapper.getLibraryDAO(user.getLibrary()), true, false, roleEntities);

        }
        return null;
    }

    private static Date stringToDateObject(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        return date;
    }

}
