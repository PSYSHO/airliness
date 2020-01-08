package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Route;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import request.GeneralRequest;
import request.Request;

import java.io.*;
import java.net.Socket;

public class AirLinesServer implements ControlInterface {
    public int pos;
    private  static Socket  Dialog;
    public Flight journal;
    public Flight secondJournal;

    public  static String path = "journal.json";
    public AirLinesServer(){}
    public AirLinesServer(Socket client){
        AirLinesServer.Dialog = client;
    }

    public Flight load(String path,Flight flight){
         Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(path);
            flight = gson.fromJson(fileReader , Flight.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flight;
    }
    public void save(String path,Flight flight){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(gson.toJson(flight));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            load(path,journal);
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
                load(path,secondJournal);
                if (secondJournal.equals(journal)){
                    journal = secondJournal;
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
                        save(path, journal);
                        break;
                    case"addFlight":
                        in.readUTF();
                        journal = (Flight)request.getObject();
                        save(path,journal);
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
       /* try {
            jsonRequest =  generalRequest.receivingRequest("updatejournal",secondJournal);
        return  jsonRequest;
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return  jsonRequest;
    }
    //todo при добавлении перелета сделать обработку по Id
    // hashmap добавить для хранения перелетов
    // сделать поиск элементов
    // блокировка элемента который используется
    // сервер перед тем как клиент редактирует принимает id
}
