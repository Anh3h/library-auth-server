package courage.library.authserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="role")
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    private String id;

    @NonNull
    private String name;

    private Boolean isAvailable;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    public RoleEntity(String id, String name, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
    }

}
