package com.techelevator.tenmo.exceptions;

public class BadImageException extends Exception {
    public BadImageException(){
        super("Unable to display image");
    }
}
