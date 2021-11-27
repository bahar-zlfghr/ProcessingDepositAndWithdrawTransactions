package com.dotin.server;

import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.model.data.Server;
import com.dotin.server.model.repository.DepositRepository;
import com.dotin.server.model.repository.ServerRepository;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : Bahar Zolfaghari
 **/
public class ServerMain {
    private static Logger logger;
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public ServerMain(Integer serverPort) throws IOException {
        logger.info("Server socket is initializing...");
        this.serverSocket = new ServerSocket(serverPort);
        this.serverSocket.setSoTimeout(60000);
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
            } catch (SocketTimeoutException e) {
                DepositRepository.saveDepositsChanges();
                closeServerSocket(serverSocket);
                break;
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
        }
    }

    private void closeServerSocket(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }

    public static void main(String[] args) {
        try {
            ServerRepository.fetchServer();
            System.setProperty("LogFilePath", ServerLogFile.getLogFilePath());
            logger = Logger.getLogger(ServerMain.class);
            DepositRepository.fetchDeposits();
            Server server = ServerRepository.getServer();
            ServerMain serverMain = new ServerMain(server.getPort());
            serverMain.run();
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }
}
