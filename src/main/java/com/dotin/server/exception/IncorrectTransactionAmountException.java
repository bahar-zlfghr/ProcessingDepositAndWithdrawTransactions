package com.dotin.server.exception;

/**
 * @author : Bahar Zolfaghari
 **/
public class IncorrectTransactionAmountException extends Exception {

    public IncorrectTransactionAmountException(String message) {
        super(message);
    }
}
