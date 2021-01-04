package ru.bolshakov.internship.dishes_rating.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bolshakov.internship.dishes_rating.dto.ErrorResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.FieldErrorDTO;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    protected final Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public ResponseExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(NotFoundException ex, WebRequest webRequest) {
        log.error("NotFoundException handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(AuthorizationFailureException.class)
    public ResponseEntity<Object> handleAuthorizationFailure(AuthorizationFailureException ex, WebRequest webRequest) {
        log.error("AuthorizationFailureException handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED, webRequest);
    }

    @ExceptionHandler(ChangingVoteUnavailable.class)
    public ResponseEntity<Object> handleChangingVoteUnavailable(ChangingVoteUnavailable ex, WebRequest webRequest) {
        log.error("ChangingVoteUnavailable handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest webRequest) {
        log.error("AccessDeniedException handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO("Access denied"), new HttpHeaders(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler(NonUniqueParamException.class)
    public ResponseEntity<Object> handleNonUniqueParam(NonUniqueParamException ex, WebRequest webRequest) {
        log.error("NonUniqueParamException handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        log.error("MethodArgumentNotValidException handling started. ", ex);
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), messageSource.getMessage(fieldError, Locale.getDefault())))
                .collect(Collectors.toList());
        return handleExceptionInternal(ex, new ErrorResponseDTO(fieldErrors), new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest webRequest) {
        log.error(ex.getClass().getName() + " handling started. ", ex);
        return handleExceptionInternal(ex, new ErrorResponseDTO("Internal server error has occurred"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }
}
