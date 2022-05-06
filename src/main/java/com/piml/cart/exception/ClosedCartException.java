package com.piml.cart.exception;

public class ClosedCartException extends RuntimeException {
    public ClosedCartException(String errorMessage) {
        super(errorMessage);
    }
}
