package com.dentalmoovi.website.models.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String m){
        super(m);
    }
}
