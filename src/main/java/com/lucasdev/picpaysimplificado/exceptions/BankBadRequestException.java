package com.lucasdev.picpaysimplificado.exceptions;

public class BankBadRequestException extends BankException {

    private static final long serialVersionUID = 1L;

    public BankBadRequestException(String message) {
        super(message);
    }
}
