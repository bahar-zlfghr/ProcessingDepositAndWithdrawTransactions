package com.dotin.terminal.model.data;

/**
 * @author : Bahar Zolfaghari
 **/
public enum TransactionStatus {
    DONE, FAILED;

    public static TransactionStatus getTransactionStatus(String status) {
        switch (status) {
            case "done":
                return DONE;
            case "failed":
                return FAILED;
            default:
                return null;
        }
    }
}
