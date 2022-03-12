package com.example.invoiceaccounting.exception;


public class EmailIsAlreadyInUseException extends RuntimeException{
    public EmailIsAlreadyInUseException(String message) {
        super(message);
    }
}
