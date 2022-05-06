package com.piml.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class ResourceExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ClosedCartException.class)
    protected ResponseEntity<?> handleCartException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cart has already been closed.");
    }

    @ExceptionHandler(OutOfStockException.class)
    protected ResponseEntity<?> handleOutOfStockException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BuyerAlreadyExistsException.class)
    protected ResponseEntity<?> handleBuyerExistsException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(EmptyCartException.class)
    protected ResponseEntity<?> emptyCartException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
