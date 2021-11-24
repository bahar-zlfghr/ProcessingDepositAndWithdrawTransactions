package com.dotin.terminal;

import com.dotin.terminal.model.data.Response;
import com.dotin.terminal.model.data.ResponseList;
import com.dotin.terminal.model.data.Terminal;
import com.dotin.terminal.model.data.TransactionStatus;
import com.dotin.terminal.model.repository.ResponseRepository;
import com.dotin.terminal.model.repository.TerminalRepository;
import com.dotin.terminal.model.repository.TransactionRepository;

import java.io.*;
import java.net.Socket;

/**
 * @author : Bahar Zolfaghari
 **/
public class TerminalMain {
    private static final String DATA_FORMAT = "%s, %s, %s";
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public TerminalMain(String serverIP, Integer serverPort) throws IOException {
        this.socket = new Socket(serverIP, serverPort);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() {
        TransactionRepository.getTransactions().forEach(transaction -> {
            try {
                // transaction -> [type, amount, depositID]
                String id = transaction.getId();
                String type = transaction.getType().toString();
                String amount = transaction.getAmount().toString();
                String depositID = transaction.getDepositID();
                String data = String.format(DATA_FORMAT, type, amount, depositID);
                // send transaction to server
                writer.write(data + "\n");
                writer.flush();
                // get response from server -> [status, description]
                String response = reader.readLine();
                String[] tokens = response.split(", ");
                TransactionStatus status = TransactionStatus.getTransactionStatus(tokens[0]);
                String description = tokens[1];
                Response serverResponse = new Response(transaction, status, description);
                ResponseList.getResponses().add(serverResponse);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        try {
            writer.write("end" + "\n");
            writer.flush();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Please enter terminal file name in src/main/resources/terminal directory: ");
            String terminalFileName = consoleReader.readLine();
            TerminalRepository.fetchTerminal(terminalFileName);
            TransactionRepository.fetchTransactions(terminalFileName);
            Terminal terminal = TerminalRepository.getTerminal();
            String serverIP = terminal.getServerIP();
            Integer serverPort = terminal.getServerPort();
            TerminalMain terminalMain = new TerminalMain(serverIP, serverPort);
            terminalMain.run();
            ResponseRepository.saveResponses("response" + terminal.getId() + ".xml");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
