package courage.library.authserver.exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

    private ServiceError serviceError;

    public NotFoundException( ServiceError serviceError ) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    public static NotFoundException create( String message, Object... args ) {
        return new NotFoundException(new ServiceError(404, MessageFormat.format(message, args)));
    }

}
