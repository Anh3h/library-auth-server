package courage.library.authserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String uuid;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private Date dob;
    private String telephone;
    private String address;

    @ManyToOne
    @JoinColumn(name="library_id")
    private LibraryEntity library;

    private Boolean enabled;
    private Boolean accountLocked;

    @NonNull
    @ManyToMany()
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles;

    public UserEntity(String uuid, String firstName, String lastName, String email, String password,
                      Date dob, String telephone, String address, LibraryEntity library,
                      Boolean enabled, Boolean accountLocked, List<RoleEntity> roles) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.telephone = telephone;
        this.address = address;
        this.library = library;
        this.enabled = enabled;
        this.roles = roles;
        this.accountLocked = accountLocked;
    }
}
