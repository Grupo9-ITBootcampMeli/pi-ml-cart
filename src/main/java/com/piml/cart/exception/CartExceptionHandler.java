package com.piml.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class CartExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
    }
}
