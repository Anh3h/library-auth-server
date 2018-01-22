package courage.library.authserver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceError {

    private int code;
    private String message;

}