package courage.library.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Library {

    private String uuid;
    @NonNull private String name;
    private String address;
    private String logo;

}
