package com.lucasdev.picpaysimplificado.exceptions;

public class BankNotificationException extends BankException {

  private static final long serialVersionUID = 1L;

    public BankNotificationException(String message) {
        super(message);
    }
}
