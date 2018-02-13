package courage.library.authserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    public RoleEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
