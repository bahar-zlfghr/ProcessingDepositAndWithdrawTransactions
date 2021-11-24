package com.dotin.server;

import com.dotin.server.model.data.Server;
import com.dotin.server.model.repository.DepositRepository;
import com.dotin.server.model.repository.ServerRepository;

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
    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    public ServerMain(Integer serverPort) throws IOException {
        System.out.println("Enter to constructor.");
        this.serverSocket = new ServerSocket(serverPort);
        this.serverSocket.setSoTimeout(60000);
        this.pool = Executors.newFixedThreadPool(5);
        System.out.println("Server socket is initialized.");
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Server socket starts listening...");
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Server socket find a new connection.");
                ServerThread serverThread = new ServerThread(connectionSocket);
                pool.execute(serverThread);
            } catch (SocketTimeoutException e) {
                DepositRepository.UpdateDeposits();
                break;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerRepository.fetchServer();
            DepositRepository.fetchDeposits();
            Server server = ServerRepository.getServer();
            ServerMain serverMain = new ServerMain(server.getPort());
            serverMain.run();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
