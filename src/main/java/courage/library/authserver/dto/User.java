package courage.library.authserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String uuid;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @JsonIgnore @NonNull private String password;
    private String dob;
    private String telephone;
    private String address;
    private Library library;
    private List<Role> roles;


    public User(String uuid, String firstName, String lastName, String email,
                String password, String dob, String telephone, String address) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.telephone = telephone;
        this.address = address;
        this.library = null;
        this.roles = new ArrayList<>();
    }
}
