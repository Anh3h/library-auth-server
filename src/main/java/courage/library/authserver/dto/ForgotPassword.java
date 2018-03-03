package courage.library.authserver.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPassword {

	@NotNull private String newPassword;
	@NotNull private String repeatNewPassword;

}
