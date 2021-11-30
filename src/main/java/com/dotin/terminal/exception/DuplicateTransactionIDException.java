package com.dotin.terminal.exception;

/**
 * @author : Bahar Zolfaghari
 **/
public class DuplicateTransactionIDException extends Exception {

    public DuplicateTransactionIDException(String message) {
        super(message);
    }
}
