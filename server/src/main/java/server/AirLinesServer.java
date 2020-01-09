package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.AirFlight;
import general.Flight;
import general.Route;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import request.GeneralRequest;
import request.Request;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;

public class AirLinesServer implements ControlInterface {
    int i = 0;
    public int pos;
    private  static Socket  Dialog;
    private HashMap flightHashMap = new HashMap();
    private HashMap updateFlight = new HashMap();
    public AirFlight journal;
    public AirFlight secondJournal;

    public  static String path = "journal.json";
    public AirLinesServer(){}
    public AirLinesServer(Socket client){
        AirLinesServer.Dialog = client;
    }

    public HashMap load(String path, AirFlight airflight,HashMap hashMap){
         Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(path);
            airflight = gson.fromJson(fileReader , (Type) Flight.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i =0;i>=airflight.getSize();i++){
            int id = airflight.getFlight(i).getId();
            Flight flight = airflight.getFlight(i);
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

    @Override
    public void run() {
        try {
            load(path,journal,flightHashMap);
            String json ="";
            String getFlight;
            DataOutputStream out = new DataOutputStream(Dialog.getOutputStream());
            DataInputStream in = new DataInputStream(Dialog.getInputStream());
            System.out.println("out created");
            System.out.println("in created");
            Gson  gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(GeneralRequest.class, new CustomConverter())
                    .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                    .registerTypeAdapter(Route.class, new CustomConverterRoute())
                    .create();
            while (!Dialog.isClosed()){
                load(path,secondJournal,updateFlight);
                if (updateFlight.equals(flightHashMap)){
                    flightHashMap = updateFlight;
                    out.writeUTF(listUpdate());
                    out.flush();
                }
                json = in.readUTF();
                Request request =gson.fromJson(json,GeneralRequest.class);
                switch(request.getMessage()) {
                    case "getflight":
                        getFlight = new Gson().toJson(journal);
                        out.writeUTF(getFlight);
                        out.flush();
                        System.out.println("Сервер отправи журнал");
                        break;
                    case "removeFlight":
                        i = request.getIndex();
                        flightHashMap.remove(i);
                        save(path, flightHashMap);
                        load(path,journal,flightHashMap);
                        getFlight = new Gson().toJson(journal);
                        out.writeUTF(getFlight);
                        out.flush();
                        break;
                    case"addFlight":
                        Flight flight = (Flight) request.getObject();
                        //journal.add(flight);
                        flightHashMap.put(flight.getId(),flight);
                        save(path,flightHashMap);
                        load(path,journal,flightHashMap);
                        break;
                    case "EditFlight":
                        Flight flightEdit=(Flight)request.getObject();
                        int indexEdit=request.getIndex();
                        i = flightEdit.getId();
                        if(journal.busy(i)==false){
                            journal.getFlight(i).isVariabilitytrue();
                        journal.refactor(i,flightEdit);
                        flightHashMap.replace(i,journal.getFlight(i));
                        save(path,flightHashMap);
                        load(path,journal,flightHashMap);
                        getFlight = new Gson().toJson(journal);
                        out.writeUTF(getFlight);
                        out.flush();}
                        else out.writeUTF("Обьект занят");
                        out.flush();
                        break;
                    case"quit":
                        in.close();
                        out.close();
                        Dialog.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String listUpdate() {
        String jsonRequest = null;
    GeneralRequest generalRequest = null;
        try {
            jsonRequest =  generalRequest.receivingRequest("updatejournal",secondJournal);
        return  jsonRequest;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }
    //todo при добавлении перелета сделать обработку по Id
    // hashmap добавить для хранения перелетов -
    // блокировка элемента который используется
    // сервер перед тем как клиент редактирует принимает id
}
