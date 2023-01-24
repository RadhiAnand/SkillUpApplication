package com.softura.skillup.exception;

import com.softura.skillup.constants.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkillUpException.class);
    private int errorCode;

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(
            RecordNotFoundException exception,
            WebRequest request
    ) {
        errorCode = getErrorCode(exception);
        LOGGER.error(exception + " ErrorCode=" + errorCode);
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request, errorCode);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        errorCode = getErrorCode(ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error. Check 'errors' field for details.",
                errorCode);


        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        LOGGER.error(errorResponse + " ErrorCode=" + errorCode);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        errorCode = getErrorCode(exception);
        LOGGER.error(exception.getStackTrace() + " ErrorCode=" + errorCode);
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request,
                errorCode
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request,
            int errorCode
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request,
                errorCode);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request,
            int errorCode
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                exception.getMessage(),
                errorCode
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }


    private int getErrorCode(Exception ex) {
        ErrorCode code = ErrorCode.valueOf(ex.getClass().getSimpleName().toUpperCase());
        return code.errorCode;
    }

}
