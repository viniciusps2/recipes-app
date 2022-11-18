package org.recipes.app.infrastructure.rest;

import org.recipes.app.application.errors.NotFoundException;
import org.recipes.app.infrastructure.dto.ErrorDTO;
import org.recipes.app.infrastructure.dto.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class ErrorControllerAdvise {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoSuchElement(NotFoundException ex) {
        var dto = new ErrorDTO(ex.getMessage());
        return status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        var dto = new ValidationErrorDTO("Error on validating fields.", errors);
        return badRequest().body(dto);
    }
}
