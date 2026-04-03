package br.com.delivery.micro.infra;

import br.com.delivery.micro.exception.DeliveryNotFoundException;
import br.com.delivery.micro.exception.ErrorCreatingDeliveryException;
import br.com.delivery.micro.exception.client.ClientNotFoundException;
import br.com.delivery.micro.exception.client.ErrorGettingClientInfoException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private Map<String, String> mapError(FieldError fieldError) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("field", fieldError.getField());
        mapping.put("message", fieldError.getDefaultMessage());

        return mapping;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapError)
                .toList();
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDatabaseError(DataAccessException ex) {
        return ResponseEntity
                .status(500)
                .body(Map.of("error", "Internal error when process delivery!"));
    }

    @ExceptionHandler(ErrorGettingClientInfoException.class)
    private ResponseEntity<DefaultErrorResponse> badGatewayHandler(ErrorGettingClientInfoException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(defaultErrorResponse);
    }

    @ExceptionHandler({
            ClientNotFoundException.class,
            DeliveryNotFoundException.class
    })
    private ResponseEntity<DefaultErrorResponse> notFoundHandler(RuntimeException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(defaultErrorResponse);
    }

    @ExceptionHandler({
            ErrorCreatingDeliveryException.class
    })
    private ResponseEntity<DefaultErrorResponse> internalErrorHandler(RuntimeException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResponse);
    }
}
