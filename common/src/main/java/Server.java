import threadedHandler.ThreadedHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    public  static void main(String[] args) {
        try{
            FileInputStream fil=new FileInputStream("common/src/main/java/general/config.properties");
            Properties property=new Properties();
            property.load(fil);
            String port=property.getProperty("may.port");
            try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
                while (true) {
                    System.out.println("Сервер подключен...");
                    System.out.println("Ожидает клиента...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Клиент подключен...");
                    Runnable r = new ThreadedHandler(clientSocket);
                    Thread thread = new Thread(r);
                    thread.start();
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
