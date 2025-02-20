package com.example.animal_feed.exception;

public class OrderNotLinkedException extends RuntimeException {
    public OrderNotLinkedException(String message) {
        super(message);
    }

}
