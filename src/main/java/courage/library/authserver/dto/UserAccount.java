package courage.library.authserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UserAccount {

    private String uuid;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @NonNull private String password;
    private Date dob;
    private String telephone;
    private String address;
    private Library library;
    @NonNull private List<Role> roles;

}
