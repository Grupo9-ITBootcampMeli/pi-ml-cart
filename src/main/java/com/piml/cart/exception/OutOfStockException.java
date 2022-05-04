package com.piml.cart.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String errorMessage) { super(errorMessage); }
}
