package cz.catparadise.exception;

import java.util.List;
import java.time.LocalDateTime;

public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;

    public ApiError(int status, String message, LocalDateTime timestamp, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public List<String> getErrors() {
        return errors;
    }
}
