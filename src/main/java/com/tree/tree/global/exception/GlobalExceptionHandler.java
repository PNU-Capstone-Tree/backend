package com.tree.tree.global.exception;

import com.tree.tree.global.base.BaseException;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ExceptionResponse>> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        final String errorMessage = getErrorMessage(e);
        ExceptionResponse response = new ExceptionResponse(errorMessage);
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(BaseException.class)
    public Mono<ResponseEntity<ExceptionResponse>> handleBaseException(final BaseException e) {
        final String errorMessage = e.getMessage();
        ExceptionResponse response = new ExceptionResponse(errorMessage);
        return Mono.just(ResponseEntity.status(e.exceptionType().httpStatus()).body(response));
    }

    private static String getErrorMessage(final BindException e) {
        final BindingResult bindingResult = e.getBindingResult();
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> getErrorMessage(
                        fieldError.getField(),
                        String.valueOf(fieldError.getRejectedValue()),
                        fieldError.getDefaultMessage())
                )
                .collect(Collectors.joining(", "));
    }

    private static String getErrorMessage(final String errorField, final String invalidValue,
                                          final String errorMessage) {
        return String.format("[%s] %s : %s", errorField, invalidValue, errorMessage);
    }
}

