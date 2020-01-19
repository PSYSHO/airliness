package server;

import general.Journal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Map;

public class ServerControll implements Runnable {
    Journal journal = new Journal();
    Map map;
    String path;
    ServerSocket serverSocket;
    boolean check = false;
    public ServerControll(ServerSocket serverSocket,Map map,String path){
        this.serverSocket = serverSocket;
        map = map;
        this.path = path;
    }
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String serverComand = bufferedReader.readLine();
            if (serverComand=="quit"){
                journal.save(path,map);
                System.out.println("Main server exiting...");
                check = true;
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
