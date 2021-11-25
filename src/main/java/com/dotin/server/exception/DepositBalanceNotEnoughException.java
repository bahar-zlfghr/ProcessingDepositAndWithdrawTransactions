package com.dotin.server.exception;

/**
 * @author : Bahar Zolfaghari
 **/
public class DepositBalanceNotEnoughException extends Exception {

    public DepositBalanceNotEnoughException(String message) {
        super(message);
    }
}
