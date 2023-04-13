package nl.slotboom.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class AppException extends RuntimeException {
    private HttpStatus status;
    private LocalDateTime timestamp;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

