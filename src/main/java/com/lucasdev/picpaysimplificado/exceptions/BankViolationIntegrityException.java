package com.lucasdev.picpaysimplificado.exceptions;

public class BankViolationIntegrityException extends BankException{

    private static final long serialVersionUID = 1L;

    public BankViolationIntegrityException(String message) {
        super(message);
    }
}
