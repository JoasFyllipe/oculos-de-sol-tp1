package io.github.JoasFyllipe.exceptions;

public class FuncionarioNotFoundException extends RuntimeException {
    public FuncionarioNotFoundException(String message) {
        super(message);
    }
}
