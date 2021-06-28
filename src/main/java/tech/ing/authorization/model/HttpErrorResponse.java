package tech.ing.authorization.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class HttpErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
