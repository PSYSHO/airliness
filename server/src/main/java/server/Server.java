package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.AirFlight;
import general.Flight;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.util.*;

public class Server {
    public HashMap generalAir = new HashMap();
    public HashMap flightHashMap = new HashMap();
    public Server(){}
    public static void main(String[] args) throws IOException {
        HashMap flightHashMap = new HashMap();
        String path = "journal.json";
        int port = 8000;
        Server conteins = new Server();
        Gson gson = new Gson();
        conteins.load(path,flightHashMap);
        List fly = new ArrayList<AirLinesServer>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try (ServerSocket server = new ServerSocket(port)
        ) {
            while (!server.isClosed()) {
                Thread client  = new Thread(new AirLinesServer(server.accept(), (ArrayList<AirLinesServer>) fly,flightHashMap ));
                client.start();
                System.out.println("accepted");
                fly.add(client);
                String serverCommand = bufferedReader.readLine();
                if(serverCommand.equals("quit")){
                    System.out.println("Main server exiting...");
                    conteins.save(path,flightHashMap);
                    server.close();
                    break;
                }
            }

        } catch (IOException e) {
        }



    }
    public HashMap load(String path,HashMap hashMap){
        AirFlight  journal = new AirFlight();
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(path);
            journal = gson.fromJson(fileReader , AirFlight.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i =0;i>=journal.getSize();i++){
            int id = journal.getFlight(i).getId();
            Flight flight = journal.getFlight(i);
            hashMap.put(id,flight);
        }
        return hashMap;
    }
    public void save(String path, HashMap hashMap){
        AirFlight airflight = new AirFlight();
        for (Object flight: hashMap.values()){
            airflight.add((Flight) flight);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(gson.toJson(airflight));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //todo сделать настройку в пом для клиента и ервера где лежат мейн классы
    // сделать сборку джарок с либами мавена
    // почитать про runnable  при измменении данных оповещать вех клиентов
    // сделать проверку уникальности по id при добавлении или редактировании
    // клиенты работают с своими данными сервер хранит у себя оригинал и сохраняет в конце своей работы

}
