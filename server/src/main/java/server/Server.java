package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        int port = 8000;
        List fly = new ArrayList<ControlInterface>();
        try (ServerSocket server = new ServerSocket(port)
        ) {
            while (!server.isClosed()) {
                Thread client = new Thread(new AirLinesServer(server.accept()));
                client.start();
                System.out.println("accepted");
                fly.add(client);
                for (Object controlInterface:fly){
                }
            }
        } catch (IOException e) {
        }


    }
}
