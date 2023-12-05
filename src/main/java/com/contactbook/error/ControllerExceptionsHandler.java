package com.contactbook.error;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionsHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionsHandler.class);

    @ExceptionHandler(CmsException.class)
    public ResponseEntity<List<ErrorInfo>> handleCmsException(CmsException ex) {
        log.error(ex.getMessage(), ex);
        ResponseEntity.BodyBuilder resp = getResponseEntityBuilder(ex);
        ErrorInfo error = new ErrorInfo(ex.getMessage());
        return resp.body(List.of(error));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ErrorInfo> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getConstraintViolations().stream()
                .map(v -> {
                    ErrorInfo error = new ErrorInfo(v.getMessage(), v.getPropertyPath().toString());
                    if (error.getField() != null) {
                        String[] properties = error.getField().split("\\.");
                        error.setField(properties[properties.length - 1]);
                    }
                    return error;
                })
                .collect(Collectors.toList());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public List<ErrorInfo> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        ErrorInfo error = new ErrorInfo(ex.getMessage());
        return List.of(error);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorInfo> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .map(this::buildErrorInfoForDataValidationException)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<List<ErrorInfo>> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        if (ex.getClass().getName().contains("DataIntegrityViolationException")) {
            return handleDataIntegrityViolationException(ex);
        }

        ResponseEntity.BodyBuilder resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorInfo error = new ErrorInfo(getRuntimeExceptionMessage(ex));
        return resp.body(List.of(error));
    }

    private ResponseEntity.BodyBuilder getResponseEntityBuilder(CmsException ex) {
        return switch (ex.getExceptionCode()) {
            case BAD_REQUEST -> ResponseEntity.badRequest();
            case UNAUTHORIZED -> ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN);
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND);
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    private String getRuntimeExceptionMessage(RuntimeException ex) {
        String exceptionName = ex.getClass().getName();
        String exceptionMessage = ex.getMessage();
        String methodName = ex.getStackTrace()[0].getMethodName();
        int lineNumber = ex.getStackTrace()[0].getLineNumber();
        return String.format("Exception: [%s] with message [%s] occurred in method [%s] at line [%d] ", exceptionName, exceptionMessage, methodName, lineNumber);
    }

    private ErrorInfo buildErrorInfoForDataValidationException(FieldError fieldError) {
        String message = fieldError.getDefaultMessage();
        String field = fieldError.getField();
        return new ErrorInfo(message, field);
    }

    private ResponseEntity<List<ErrorInfo>> handleDataIntegrityViolationException(RuntimeException ex) {
        Throwable cause = ex.getCause();
        if (!(cause instanceof org.hibernate.exception.ConstraintViolationException) || cause.getCause() == null) {
            throw ex;
        }
        String message;
        Throwable rootCause = cause.getCause();
        if (StringUtils.containsIgnoreCase(rootCause.getMessage(), "DUPLICATE")) {
            message = "dataDuplication";
        } else if (StringUtils.containsIgnoreCase(rootCause.getMessage(), "REFERENCE")) {
            message = "dataInUse";
        } else {
            throw ex;
        }
        ErrorInfo error = new ErrorInfo(message);
        return ResponseEntity.badRequest()
                .body(List.of(error));
    }
}
