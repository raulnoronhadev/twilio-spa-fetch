package twilio_spa_fetch_backend.exception;

import com.twilio.exception.ApiException;
import com.twilio.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleTwilioAuthentication(AuthenticationException e, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Twilio rejected the account credentials", request);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleTwilioApi(ApiException e, HttpServletRequest request) {
        // Propagate Twilio's status when it is a meaningful client error (e.g. 404
        // for an unknown SID); everything else surfaces as a bad gateway.
        Integer twilioStatus = e.getStatusCode();
        HttpStatus status = (twilioStatus != null && twilioStatus >= 400 && twilioStatus < 500)
                ? HttpStatus.valueOf(twilioStatus)
                : HttpStatus.BAD_GATEWAY;
        log.warn("Twilio API error ({}): {}", twilioStatus, e.getMessage());
        return build(status, "Twilio API error: " + e.getMessage(), request);
    }

    @ExceptionHandler(NoSuchKeyException.class)
    public ResponseEntity<ErrorResponse> handleMissingBackup(NoSuchKeyException e, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "Backup file not found", request);
    }

    @ExceptionHandler({SdkException.class, StorageException.class})
    public ResponseEntity<ErrorResponse> handleStorage(RuntimeException e, HttpServletRequest request) {
        log.error("Object storage error", e);
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Object storage error: " + e.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception processing {} {}", request.getMethod(), request.getRequestURI(), e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal error", request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.of(status.value(), status.getReasonPhrase(), message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
