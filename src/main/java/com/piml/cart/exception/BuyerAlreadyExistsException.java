package com.piml.cart.exception;

public class BuyerAlreadyExistsException extends RuntimeException {
    public BuyerAlreadyExistsException(String errorMessage) { super(errorMessage); }
}
