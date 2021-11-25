package com.dotin.terminal;

import com.dotin.terminal.model.data.*;
import com.dotin.terminal.model.repository.ResponseRepository;
import com.dotin.terminal.model.repository.TerminalRepository;
import com.dotin.terminal.model.repository.TransactionRepository;
import org.apache.log4j.Logger;

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
    private static Logger logger;

    public TerminalMain(String serverIP, Integer serverPort) throws IOException {
        this.socket = new Socket(serverIP, serverPort);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() {
        logger.info("Terminal run method started...");
        TransactionRepository.getTransactions().forEach(transaction -> {
            try {
                String type = transaction.getType().toString();
                String amount = transaction.getAmount().toString();
                String depositID = transaction.getDepositID();
                String data = String.format(DATA_FORMAT, type, amount, depositID);
                writer.write(data + "\n");
                writer.flush();
                logger.info("Terminal send the transactions one by one with [type, amount, depositID] format to server");
                String response = reader.readLine();
                String[] tokens = response.split(", ");
                TransactionStatus status = TransactionStatus.getTransactionStatus(tokens[0]);
                String description = tokens[1];
                logger.info("Terminal receive the server response with [status, description] format from server");
                Response serverResponse = new Response(transaction, status, description);
                ResponseList.getResponses().add(serverResponse);
                logger.info("Terminal save the server response per transaction in Response entity");
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
        });
        try {
            writer.write("end" + "\n");
            writer.flush();
            logger.info("Terminal after send the all transactions to server, send 'end' keyword to server");
            reader.close();
            writer.close();
            socket.close();
            logger.info("reader, writer and socket resources closed");
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }

    public static void main(String[] args) {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Please enter terminal file name in src/main/resources/terminal directory: ");
            String terminalFileName = consoleReader.readLine();
            TerminalRepository.fetchTerminal(terminalFileName);
            System.setProperty("LogFilePath", TerminalLogFile.getLogFilePath());
            logger = Logger.getLogger(TerminalMain.class);
            TransactionRepository.fetchTransactions(terminalFileName);
            Terminal terminal = TerminalRepository.getTerminal();
            String serverIP = terminal.getServerIP();
            Integer serverPort = terminal.getServerPort();
            TerminalMain terminalMain = new TerminalMain(serverIP, serverPort);
            terminalMain.run();
            ResponseRepository.saveResponses("response" + terminal.getId() + ".xml");
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }
}
