package com.dotin.terminal.model.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Bahar Zolfaghari
 **/
public class Response {

    @Getter @Setter
    private Transaction transaction;

    @Getter @Setter
    private TransactionStatus status;

    @Getter @Setter
    private String description;

    public Response(Transaction transaction, TransactionStatus status, String description) {
        this.transaction = transaction;
        this.status = status;
        this.description = description;
    }
}
