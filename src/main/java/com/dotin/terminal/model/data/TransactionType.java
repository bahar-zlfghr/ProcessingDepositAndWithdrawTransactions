package com.dotin.terminal.model.data;

/**
 * @author : Bahar Zolfaghari
 **/
public enum TransactionType {
    DEPOSIT, WITHDRAW;

    public static TransactionType getTransactionType(String type) {
        switch (type) {
            case "deposit":
                return DEPOSIT;
            case "withdraw":
                return WITHDRAW;
            default:
                return null;
        }
    }
}
