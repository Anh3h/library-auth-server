package courage.library.authserver.exception;

import java.text.MessageFormat;

public class ForbiddenException extends RuntimeException {

    private ServiceError serviceError;

    public ForbiddenException( ServiceError serviceError ) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    public static BadRequestException create( String message, Object... args ) {
        return new BadRequestException(new ServiceError(403, MessageFormat.format(message, args)));
    }

}
