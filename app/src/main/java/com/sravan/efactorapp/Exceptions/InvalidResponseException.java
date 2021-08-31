package com.sravan.efactorapp.Exceptions;

public class InvalidResponseException extends Exception {
    public InvalidResponseException() {
        super("Invalid response received.");
    }
}
