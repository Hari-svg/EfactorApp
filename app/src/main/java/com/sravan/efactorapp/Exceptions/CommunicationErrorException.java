package com.sravan.efactorapp.Exceptions;

public class CommunicationErrorException extends Exception {
    public CommunicationErrorException() {
        super("Communication error.");
    }
}
