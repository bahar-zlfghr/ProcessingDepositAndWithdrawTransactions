package com.dotin.server.model.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Bahar Zolfaghari
 **/
public class Server {

    @Getter @Setter
    private Integer port;

    @Getter @Setter
    private String outLogPath;
}
