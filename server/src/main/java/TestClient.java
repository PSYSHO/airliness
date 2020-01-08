package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
    public static void main(String[] args) {
        int i =0;
        String host = "127.0.0.1";
        int port = 6666;
        try (
                Socket socket = new Socket(host, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
        ){
            System.out.println("Client worked");
            while(i!=1){
                out.writeUTF("getflight");
                out.flush();
                Thread.sleep(10);
                System.out.println("Waiting Data from server");
                String inp = in.readUTF();
                System.out.println(inp);
                Thread.sleep(10000);
                i++;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    }
