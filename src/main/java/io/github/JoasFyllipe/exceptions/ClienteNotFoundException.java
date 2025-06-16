package io.github.JoasFyllipe.exceptions;

public class ClienteNotFoundException extends RuntimeException{
    
    public ClienteNotFoundException(String messege){
        super(messege);
    }
}
