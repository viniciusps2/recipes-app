package org.recipes.app.interfaces.rest;

import org.recipes.app.interfaces.dto.ValidationErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;

@ControllerAdvice
public class ValidationControllerAdvise {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ValidationErrorDTO> handleNoSuchElement(NoSuchElementException ex) {
        return notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ValidationErrorDTO error = new ValidationErrorDTO("Error on validating fields.", errors);
        return badRequest().body(error);
    }
}
