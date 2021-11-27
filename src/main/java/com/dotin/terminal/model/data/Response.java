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

    public Response(TransactionStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
