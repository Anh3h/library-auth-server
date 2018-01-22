package courage.library.authserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="library")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String uuid;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    private String logo;
    private Boolean isAvailable;

    public LibraryEntity(String uuid, String name, String address, String logo, Boolean isAvailable) {
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.logo = logo;
        this.isAvailable = isAvailable;
    }
}
