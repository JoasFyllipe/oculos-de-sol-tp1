package io.github.JoasFyllipe.exceptions;

public class ModelNotFoundException extends RuntimeException{

    public ModelNotFoundException(String messege){
        super(messege);
    }
}
