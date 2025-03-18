package io.github.JoasFyllipe.model.exceptions;

public class ModelNotFoundException extends RuntimeException{

    public ModelNotFoundException(String messege){
        super(messege);
    }
}
