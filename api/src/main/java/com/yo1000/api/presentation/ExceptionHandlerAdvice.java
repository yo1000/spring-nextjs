package com.yo1000.api.presentation;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleBadRequestByConstraintViolation(ConstraintViolationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setProperty("parameters", e.getConstraintViolations().stream()
                .map(violation -> Map.entry(violation.getPropertyPath().toString(), violation.getInvalidValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBadRequestByMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setProperty("parameters", e.getBindingResult().getAllErrors().stream()
                .filter(Objects::nonNull)
                .map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        return new AbstractMap.SimpleEntry<>(fieldError.getField(), fieldError.getRejectedValue());
                    } else {
                        return new AbstractMap.SimpleEntry<>(
                                objectError.getObjectName() + "." + objectError.getCode(),
                                Arrays.stream(Optional.ofNullable(objectError.getArguments()).orElse(new Object[0]))
                                        .map(o -> Optional.ofNullable(o).map(Object::toString).orElse(""))
                                        .collect(Collectors.joining(", ")));
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return problemDetail;
    }
}
