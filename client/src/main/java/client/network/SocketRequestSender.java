package client.network;

import client.util.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import shared.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketRequestSender {
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final ObjectMapper objectMapper;

    public SocketRequestSender() throws IOException {
        Config config = Config.getConfig("connection");
        socket = new Socket(
                config.getProperty(String.class,"ipAddress"),
                config.getProperty(Integer.class,"port"));
        printStream = new PrintStream(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
        objectMapper = new ObjectMapper();
    }

    public Response sendRequest(Request request) throws IOException {
        try{
            printStream.println(objectMapper.writeValueAsString(request));
            return objectMapper.readValue(scanner.nextLine(), Response.class);
        } catch(Exception e){
            close();
            throw e;
        }
    }

    public void close(){
        try{
            scanner.close();
            printStream.close();
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
