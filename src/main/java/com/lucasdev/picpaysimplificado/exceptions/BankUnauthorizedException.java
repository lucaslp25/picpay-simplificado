package com.lucasdev.picpaysimplificado.exceptions;

public class BankUnauthorizedException extends BankException {

  private static final long serialVersionUID = 1L;

    public BankUnauthorizedException(String message) {
        super(message);
    }
}
