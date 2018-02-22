package courage.library.authserver.dto.Message;

import java.util.List;

import courage.library.authserver.dto.Library;
import courage.library.authserver.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {

	private String uuid;
	@NonNull private String firstName;
	@NonNull private String lastName;
	@NonNull private String email;
	private String dob;
	private String telephone;
	private String address;
	private Library library;
	private List<Role> roles;
	private String action;

}
