package courage.library.authserver.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler( value = NotFoundException.class )
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex ) {
        return new ResponseEntity<>( new ServiceError(404, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler( value = BadRequestException.class )
    protected ResponseEntity<Object> handleBadRequest( RuntimeException ex ) {
        return new ResponseEntity<>( new ServiceError(400, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler( value = ConflictException.class )
    protected ResponseEntity<Object> handleConflict(RuntimeException ex ) {
        return new ResponseEntity<>( new ServiceError(409, ex.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT);
    }

}
