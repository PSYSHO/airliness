package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Journal;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class Server {
    public Map flightMap = new HashMap();

    public Server() {
    }

    public static void main(String[] args) throws IOException {
        Journal journal = new Journal();
        Map<Integer, Flight> flightMap = new HashMap<Integer, Flight>();
        FileInputStream fil = new FileInputStream("common/src/main/java/general/config.properties");
        Properties property = new Properties();
        property.load(fil);
        String port = property.getProperty("may.port");
        String path = property.getProperty("may.path");
        File file = new File(path);
        if (file.exists()) ;
        else journal.create();
        journal.load(path, flightMap);
        List clientList = new ArrayList<AirLinesServer>();
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Thread waitingExit = new Thread(new Runnable()  {
                @Override
                public void run() {
                    boolean flag = true;
                    while(flag) {
                        try {
                            /*if (br.ready()) {*/
                                        /*System.out.println("Поток читает сообщение...");
                                        String servercomand = "";
                                        servercomand = br.readLine();*/
                            if (br.readLine().equalsIgnoreCase("quit")) {
                                System.out.println("Main server exiting...");
                                journal.save(path, flightMap);
                                flag = false;
                                server.close();
                            } else
                                System.out.println("Неизвестная команда...");
                            /*}*/
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Поток закончил свою работу...");
                }
            });
            waitingExit.start();
            System.out.println("Сервер подключен...");
            while (!server.isClosed()) {
                try {
                    System.out.println("Ожидает клиента...");
                    Socket socketClient = server.accept();
                    Thread client = new Thread(new AirLinesServer(socketClient, (ArrayList<AirLinesServer>) clientList, flightMap));
                    client.start();
                    System.out.println("accepted");
                    clientList.add(client);
                } catch (SocketException e) {
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Cервер закончил свою работу...");
//todo сделать генераию файла в случае если его нет
    }
}
//todo сделать настройку в пом для клиента и ервера где лежат мейн классы
// клиенты работают с своими данными сервер хранит у себя оригинал и сохраняет в конце своей работы

