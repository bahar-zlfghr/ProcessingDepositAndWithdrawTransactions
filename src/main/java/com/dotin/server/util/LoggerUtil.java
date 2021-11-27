package com.dotin.server.util;

import org.apache.log4j.Logger;

/**
 * @author : Bahar Zolfaghari
 **/
public interface LoggerUtil {

    static Logger getLogger(Class<?> clazz, String path) {
        System.setProperty("LogFilePath", path);
        return Logger.getLogger(clazz);
    }
}
