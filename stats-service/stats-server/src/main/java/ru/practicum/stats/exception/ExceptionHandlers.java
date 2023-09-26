package ru.practicum.stats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.exception.ApiError;
import ru.practicum.stats.exception.DateException;


import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError exceptionHandler(final DateException e) {
        return new ApiError.ApiErrorBuilder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
