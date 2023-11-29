package com.bci.reactive.web;

import com.bci.reactive.exception.EmailNonUniqueResultException;
import com.bci.reactive.exception.ErrorCode;
import com.bci.reactive.exception.PhoneNonUniqueResultException;
import com.bci.reactive.webdto.response.BaseWebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionRestController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseWebResponse> handleEntityNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseWebResponse.error(ErrorCode.ENTITY_NOT_FOUND));
    }

    @ExceptionHandler(EmailNonUniqueResultException.class)
    public ResponseEntity<BaseWebResponse> handleEmailNonUniqueResultException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseWebResponse.error(ErrorCode.ENTITY_EMAIL_USER_DUPLICATED));
    }

    @ExceptionHandler(PhoneNonUniqueResultException.class)
    public ResponseEntity<BaseWebResponse> handlePhoneNonUniqueResultException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseWebResponse.error(ErrorCode.ENTITY_PHONE_USER_DUPLICATED));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}