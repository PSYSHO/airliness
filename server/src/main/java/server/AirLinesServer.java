package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Route;
import general.TypeMessage;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import request.GeneralRequest;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static general.TypeMessage.deleteFlight;

public class AirLinesServer implements ControlInterface {
    List fly = new ArrayList<AirLinesServer>();
    int i = 0;
    public int pos;
    private  static Socket  Dialog;
    private HashMap flights = new HashMap();
    public AirLinesServer(){}
    public AirLinesServer(Socket client){
        AirLinesServer.Dialog = client;
    }
    public AirLinesServer(Socket client,ArrayList<AirLinesServer> arrayList,HashMap hashMap){
        AirLinesServer.Dialog = client;
        fly = arrayList;
        flights = hashMap;
    }

    @Override
    public void run() {
        try {
            AirLinesServer airLinesServer = null;
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
                Flight journal = new Flight();
                json = in.readUTF();
                Request request =gson.fromJson(json,GeneralRequest.class);
                switch(request.getMessage()) {
                    case "String.valueOf(TypeMessage.getFlight)":
                        getFlight = new Gson().toJson(flights);
                        out.writeUTF(getFlight);
                        out.flush();
                        System.out.println("Сервер отправи журнал");
                        break;
                    case "String.valueOf(deleteFlight)":
                        i = request.getIndex();
                        journal = (Flight) flights.get(i);
                        if(journal.isVariability()==false){
                            flights.remove(i);
                        }else {
                            out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                            out.flush(); }
                        getFlight = new Gson().toJson(flights);
                        out.writeUTF(getFlight);
                        out.flush();
                        airLinesServer.Update();
                        break;
                    case "String.valueOf(TypeMessage.addFlight)":
                        Flight flight = (Flight) request.getObject();
                        flights.put(flight.getId(),flight);
                        airLinesServer.Update();
                        break;
                    case "String.valueOf(TypeMessage.editFlight)":
                        Flight flightEdit=(Flight)request.getObject();
                        if(flightEdit.isVariability()==false) {
                            flightEdit.isVariabilitytrue();
                            flights.replace(flightEdit.getId(), flightEdit);
                        }else{ out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));}
                        getFlight = new Gson().toJson(flights);
                        out.writeUTF(getFlight);
                        out.flush();
                        airLinesServer.Update();
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
    public void Update() throws IOException {
        DataOutputStream out = new DataOutputStream(Dialog.getOutputStream());
        DataInputStream in = new DataInputStream(Dialog.getInputStream());
        for(Object o:fly){
            AirLinesServer airLinesServer = (AirLinesServer) o;
            String getFlight = new Gson().toJson(flights);
            try {
                out.writeUTF(String.valueOf(TypeMessage.update));
                out.writeUTF(getFlight);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //todo при добавлении перелета сделать обработку по Id
    // hashmap добавить для хранения перелетов -
    // блокировка элемента который используется
    // сервер перед тем как клиент редактирует принимает id
}
