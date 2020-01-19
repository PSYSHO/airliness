package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Journal;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.util.*;

public class Server {
    public Map flightMap = new HashMap();

    public Server() {
    }

    public static void main(String[] args) throws IOException {
        Journal journal = new Journal();
        Map flightMap = new HashMap();
        Gson gson = new Gson();
        FileInputStream fil = new FileInputStream("config.properties");
        Properties property = new Properties();
        property.load(fil);
        String port = property.getProperty("may.port");
        String path = property.getProperty("may.path");
        File file = new File("journal.json");
        if(file.exists());else journal.save(path,flightMap);
        journal.load(path, flightMap);
        List clientList = new ArrayList<AirLinesServer>();
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (!server.isClosed()) {
                System.out.println("Сервер подключен...");
                System.out.println("Ожидает клиента...");
                if(br.ready()){System.out.println("Main server found messages");}
                String servercomand = br.readLine();
                if (servercomand.equalsIgnoreCase("quit")){
                    System.out.println("Main server exiting...");
                    journal.save(path,flightMap);
                    server.close();
                }
                Thread client = new Thread(new AirLinesServer(server.accept(), (ArrayList<AirLinesServer>) clientList, flightMap));
                client.start();
                System.out.println("accepted");
                clientList.add(client);
            }
        } catch (IOException e) {
        }

//todo сделать генераию файла в случае если его нет
    }
}
//todo сделать настройку в пом для клиента и ервера где лежат мейн классы
// клиенты работают с своими данными сервер хранит у себя оригинал и сохраняет в конце своей работы

