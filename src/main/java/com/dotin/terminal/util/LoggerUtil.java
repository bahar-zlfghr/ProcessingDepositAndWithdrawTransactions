package com.dotin.terminal.util;

import com.dotin.terminal.model.data.TerminalLogFile;
import com.dotin.terminal.model.repository.TransactionRepository;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public interface LoggerUtil {

    static Logger getLogger(Class<?> clazz, String path) {
        System.setProperty("LogFilePath", path);
        return Logger.getLogger(clazz);
    }

    static void exceptionsLog(List<Exception> exceptions) {
        Logger logger = LoggerUtil.getLogger(TransactionRepository.class, TerminalLogFile.getLogFilePath());
        exceptions.forEach(e -> logger.error(e.getMessage(), e));
    }
}
