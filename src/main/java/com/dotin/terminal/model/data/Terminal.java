package com.dotin.terminal.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class Terminal {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private TerminalType type;

    @Getter @Setter
    private Server server;

    @Getter @Setter
    private List<Transaction> transactions = new ArrayList<>();

    @Getter @Setter
    private String outLogPath;

    public Terminal(String id, TerminalType type) {
        this.id = id;
        this.type = type;
    }
}
