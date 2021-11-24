package com.dotin.terminal.model.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Bahar Zolfaghari
 **/
public class Terminal {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private TerminalType type;

    @Getter @Setter
    private String serverIP;

    @Getter @Setter
    private Integer serverPort;

    @Getter @Setter
    private String outLogPath;
}
