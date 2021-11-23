package com.dotin.terminal.model.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Bahar Zolfaghari
 **/
public class Server {

    @Getter @Setter
    private String ip;

    @Getter @Setter
    private String port;

    public Server(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }
}
