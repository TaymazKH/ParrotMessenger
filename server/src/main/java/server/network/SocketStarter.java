package server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.logic.ClientHandler;
import server.logic.DataHolder;
import server.util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketStarter extends Thread {
    private static final Logger logger = LogManager.getLogger(SocketStarter.class);
    private final DataHolder dataHolder;

    public SocketStarter(DataHolder dataHolder){
        this.dataHolder = dataHolder;
    }

    @Override
    public void run() {
        logger.info("Started working");
        try {
            ServerSocket serverSocket = new ServerSocket(Config.getConfig("connection").getProperty(Integer.class,"port"));
            while(true){
                Socket socket = serverSocket.accept();
                new ClientHandler(new SocketResponseSender(socket),dataHolder).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
