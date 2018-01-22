package courage.library.authserver.exception;

import java.text.MessageFormat;

public class BadRequestException extends RuntimeException {

    private ServiceError serviceError;

    public BadRequestException( ServiceError serviceError ) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    public static BadRequestException create( String message, Object... args ) {
        return new BadRequestException(new ServiceError(400, MessageFormat.format(message, args)));
    }

}
