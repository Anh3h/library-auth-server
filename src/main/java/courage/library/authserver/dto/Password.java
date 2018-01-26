package courage.library.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Password {

    @NotNull private String oldPassword;
    @NotNull private String newPassword;

}
