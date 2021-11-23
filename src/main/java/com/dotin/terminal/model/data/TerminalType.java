package com.dotin.terminal.model.data;

/**
 * @author : Bahar Zolfaghari
 **/
public enum TerminalType {
    ATM;

    public static TerminalType getTerminalType(String type) {
        if ("atm".equalsIgnoreCase(type)) {
            return ATM;
        }
        return null;
    }
}
