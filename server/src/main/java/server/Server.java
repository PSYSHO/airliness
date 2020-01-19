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
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))
        ) {
            Thread controll = new Thread(new ServerControll(server, flightMap, path));
            controll.start();
            while (!server.isClosed()) {
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

