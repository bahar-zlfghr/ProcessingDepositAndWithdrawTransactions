package com.dotin.server;

import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.model.data.Server;
import com.dotin.server.model.repository.DepositRepository;
import com.dotin.server.model.repository.ServerRepository;
import com.dotin.server.util.LoggerUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Bahar Zolfaghari
 **/
public class ServerMain {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private static Logger logger;

    public ServerMain(Integer serverPort) throws IOException {
        logger.info("Server socket is initializing...");
        this.serverSocket = new ServerSocket(serverPort);
        this.pool = Executors.newFixedThreadPool(5);
        logger.info("Server socket is initialized on " + serverPort + " port");
    }

    public void run() {
        while (true) {
            try {
                logger.info("Server socket starts listening on " + serverSocket.getLocalPort() + "...");
                Socket connectionSocket = serverSocket.accept();
                logger.info("Server socket find a new connection");
                ServerThread serverThread = new ServerThread(connectionSocket);
                pool.execute(serverThread);
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerRepository.fetchServer();
            logger = LoggerUtil.getLogger(ServerMain.class, ServerLogFile.getLogFilePath());
            DepositRepository.fetchDeposits();
            Server server = ServerRepository.getServer();
            ServerMain serverMain = new ServerMain(server.getPort());
            serverMain.run();
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }
}
