package nl.slotboom.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class AppException extends RuntimeException {
    private HttpStatus status;
    private LocalDateTime timestamp;

    // custom exception with a specified message and HTTP status, and sets the timestamp to the current time
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Get HTTP status
    public HttpStatus getStatus() {
        return status;
    }

    // Get current timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

