package nl.slotboom.exceptions;

import nl.slotboom.models.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
    This is a global exception handler for the application that handles instances of AppException and returns
    an HTTP response with an ErrorResponse object containing the error status code, message, and timestamp.
 */
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().value(), ex.getMessage(), ex.getTimestamp());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}




