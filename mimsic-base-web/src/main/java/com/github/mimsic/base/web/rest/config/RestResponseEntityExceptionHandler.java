package com.github.mimsic.base.web.rest.config;

import com.github.mimsic.base.common.message.ErrorMessage;
import com.github.mimsic.base.common.message.ErrorWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorWrapper<String> errorWrapper = new ErrorWrapper<>(
                status.value(),
                status.getReasonPhrase(),
                ErrorMessage.INVALID_JSON_STRUCTURE,
                request.getDescription(false));
        return handleExceptionInternal(ex, errorWrapper, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorWrapper<String> errorWrapper = new ErrorWrapper<>(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false));
        return handleExceptionInternal(ex, errorWrapper, headers, status, request);
    }

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<Object> handleAbruptError(Exception ex, WebRequest request) {
        ErrorWrapper<String> errorWrapper = new ErrorWrapper<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ErrorMessage.UNEXPECTED_ERROR,
                request.getDescription(false));
        LOGGER.error("", ex);
        return new ResponseEntity<>(errorWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public final ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        ErrorWrapper<String> errorWrapper = new ErrorWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorWrapper, HttpStatus.BAD_REQUEST);
    }
}
