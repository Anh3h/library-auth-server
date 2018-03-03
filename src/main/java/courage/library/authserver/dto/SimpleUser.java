package courage.library.authserver.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SimpleUser {

    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @NonNull private String password;
    private Date dob;
    private String telephone;
    private String address;

}
