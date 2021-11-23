package com.dotin.server.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class Server {

    @Getter @Setter
    private Integer port;

    @Getter @Setter
    private List<Deposit> deposits = new ArrayList<>();

    @Getter @Setter
    private String outLogPath;
}
