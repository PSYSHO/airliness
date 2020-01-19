package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Route;
import general.TypeMessage;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import request.GeneralMessage;
import request.ListFromServer;
import request.Message;
import request.MessageFromServer;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AirLinesServer implements ControlInterface {
    List fly = new ArrayList<AirLinesServer>();
    int i = 0;
    public int pos;
    private static Socket Dialog;
    private HashMap flightsMap = new HashMap();
    List flights = new ArrayList<Flight>();

    public AirLinesServer() {
    }

    public AirLinesServer(Socket client) {
        AirLinesServer.Dialog = client;
    }

    public AirLinesServer(Socket client, ArrayList<AirLinesServer> arrayList, HashMap hashMap) {
        AirLinesServer.Dialog = client;
        fly = arrayList;
        flightsMap = hashMap;
    }

    @Override
    public void run() {
        try {
            for(Object fly: flightsMap.values()){
                flights = (List) fly;
            }
            TypeMessage typeMessage;
            AirLinesServer airLinesServer = null;
            String json = "";
            String getFlight;
            DataOutputStream out = new DataOutputStream(Dialog.getOutputStream());
            DataInputStream in = new DataInputStream(Dialog.getInputStream());
            System.out.println("out created");
            System.out.println("in created");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(GeneralMessage.class, new CustomConverter())
                    .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                    .registerTypeAdapter(Route.class, new CustomConverterRoute())
                    .create();
            while (!Dialog.isClosed()) {
                Flight journal = new Flight();
                json = in.readUTF();
                Message request = gson.fromJson(json, GeneralMessage.class);
                switch (request.getMessage()) {
                    case getFlight:
                        MessageFromServer messageFromServer = new ListFromServer(TypeMessage.getFlight, flights);
                        getFlight = new Gson().toJson(messageFromServer);
                        out.writeUTF(getFlight);
                        out.flush();
                        break;
                    case deleteFlight:
                        i = request.getIndex();
                        journal = (Flight) flightsMap.get(i);
                        if (journal.isVariability() == false) {
                            flightsMap.remove(i);
                        } else {

                            out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                            out.flush();
                        }
                        getFlight = new Gson().toJson(flightsMap);
                        out.writeUTF(getFlight);
                        out.flush();
                        airLinesServer.Update();
                        break;
                    case addFlight:
                        Flight flight = (Flight) request.getObject();
                        flightsMap.put(flight.getId(), flight);
                        airLinesServer.Update();
                        break;
                    case editFlight:
                        Flight flightEdit = (Flight) request.getObject();
                        if (flightEdit.isVariability() == false) {
                            flightEdit.isVariabilitytrue();
                            flightsMap.replace(flightEdit.getId(), flightEdit);
                        } else {
                            out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                        }
                        getFlight = new Gson().toJson(flightsMap);
                        out.writeUTF(getFlight);
                        out.flush();
                        airLinesServer.Update();
                        break;
                    case quit:
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
        for (Object o : fly) {
            AirLinesServer airLinesServer = (AirLinesServer) o;
            String getFlight = new Gson().toJson(flightsMap);
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
