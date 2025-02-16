package com.example.animal_feed.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super(message);
    }

}
