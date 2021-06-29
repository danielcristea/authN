package tech.ing.authorization.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.ing.authorization.model.HttpErrorResponse;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpErrorResponse> badCredentialsException() {
        return createHttpErrorResponse(BAD_REQUEST, "Invalid username / password");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpErrorResponse> usernameNotFoundException() {
        return createHttpErrorResponse(BAD_REQUEST, "Invalid username / password");
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<HttpErrorResponse> tokenExpiredException(InsufficientAuthenticationException exception) {
        return createHttpErrorResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpErrorResponse> methodNotSupportedException() {
        return createHttpErrorResponse(METHOD_NOT_ALLOWED, "Method is not allowed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorResponse> internalServerErrorException(Exception exception) {
        log.error(exception.getMessage());
        return createHttpErrorResponse(INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
    }

    private ResponseEntity<HttpErrorResponse> createHttpErrorResponse(HttpStatus httpStatus, String message) {
        HttpErrorResponse errorResponse = HttpErrorResponse.builder()
                .httpStatus(httpStatus)
                .httpStatusCode(httpStatus.value())
                .reason(httpStatus.getReasonPhrase())
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
