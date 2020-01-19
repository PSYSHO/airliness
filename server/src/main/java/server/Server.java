package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.util.*;

public class Server {
    public HashMap flightHashMap = new HashMap();

    public Server() {
    }

    public static void main(String[] args) throws IOException {
        HashMap flightHashMap = new HashMap();
        String path = "journal.json";
        Server conteins = new Server();
        Gson gson = new Gson();
        FileInputStream fil = new FileInputStream("config.properties");
        Properties property = new Properties();
        property.load(fil);
        String port = property.getProperty("may.port");
        conteins.load(path, flightHashMap);
        List fly = new ArrayList<AirLinesServer>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))
        ) {
            while (!server.isClosed()) {
                Thread client = new Thread(new AirLinesServer(server.accept(), (ArrayList<AirLinesServer>) fly, flightHashMap));
                client.start();
                System.out.println("accepted");
                fly.add(client);
                String serverCommand = bufferedReader.readLine();
                if (serverCommand.equals("quit")) {
                    conteins.save(path, flightHashMap);
                    System.out.println("Main server exiting...");
                    conteins.save(path, flightHashMap);
                    server.close();
                    break;
                }
            }

        } catch (IOException e) {
        }

//todo сделать генераию файла в случае если его нет
    }

    public HashMap load(String path, HashMap hashMap) {
        List journal = new ArrayList<Flight>();
        Gson gson = new Gson();
        gson.toJson(flightHashMap);
        try {
            FileReader fileReader = new FileReader("JournalFlights");
            journal = gson.fromJson(fileReader, (Type) Flight.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int k = journal.size();
        for (int i = 0; i < k; i++) {
            Flight flight = new Flight();
            flight = (Flight) journal.get(i);
            int id  = flight.getId();
            hashMap.put(id,journal.get(i));
        }
        return hashMap;
    }

    public void save(String path, HashMap hashMap) {
        List flights = new ArrayList<Flight>();
        for (Object flight : hashMap.values()) {
            flights.add((Flight) flight);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter("JournalFlights");
            fileWriter.write(gson.toJson(flights));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //todo сделать настройку в пом для клиента и ервера где лежат мейн классы
    // клиенты работают с своими данными сервер хранит у себя оригинал и сохраняет в конце своей работы

}
