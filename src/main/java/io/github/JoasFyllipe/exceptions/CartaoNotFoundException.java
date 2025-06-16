package io.github.JoasFyllipe.exceptions;

public class CartaoNotFoundException extends RuntimeException {
    public CartaoNotFoundException(String message) {
        super(message);
    }
}
