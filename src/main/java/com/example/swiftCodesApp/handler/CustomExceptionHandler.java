package com.example.swiftCodesApp.handler;

import com.example.swiftCodesApp.dto.ErrorMessageDto;
import com.example.swiftCodesApp.exception.ConflictException;
import com.example.swiftCodesApp.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequestException(MethodArgumentNotValidException e,
                                                                     HttpServletRequest request) {
        ErrorMessageDto error = new ErrorMessageDto(
                request.getRequestURI(),
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.info(error.message());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNotFoundException(NotFoundException e,
                                                                     HttpServletRequest request) {
        ErrorMessageDto error = new ErrorMessageDto(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        log.info(error.message());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessageDto> handleConflictException(NotFoundException e,
                                                                   HttpServletRequest request) {
        ErrorMessageDto error = new ErrorMessageDto(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        log.info(error.message());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(Exception e,
                                                           HttpServletRequest request) {
        ErrorMessageDto error = new ErrorMessageDto(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        log.info(error.message());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
