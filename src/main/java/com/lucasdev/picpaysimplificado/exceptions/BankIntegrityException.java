package com.lucasdev.picpaysimplificado.exceptions;

public class BankIntegrityException extends BankException{

    private static final long serialVersionUID = 1L;

    public BankIntegrityException(String message) {
        super(message);
    }
}
