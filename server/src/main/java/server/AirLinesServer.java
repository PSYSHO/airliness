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
import java.util.*;

public class AirLinesServer implements ControlInterface {

    private int i = 0;
    private static Socket Dialog;
    private volatile Map<Integer, Flight> flightsMap = new HashMap<Integer, Flight>();
    private volatile List<Flight> flights = new ArrayList<Flight>();
    private DataOutputStream  out;
    private DataInputStream  in;

    public DataOutputStream getOut(){
        return out;
    }

    public AirLinesServer() {
    }

    public AirLinesServer(Socket client, Map<Integer,Flight> map) {
        AirLinesServer.Dialog = client;
        flightsMap = map;
    }

    @Override
    public void run() {
        try {
            flights=updateFlights(flightsMap);
            String json = "";
            String getFlight;
            out = new DataOutputStream(Dialog.getOutputStream());
            in = new DataInputStream(Dialog.getInputStream());
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
                Message message = gson.fromJson(json, GeneralMessage.class);
                switch (message.getMessage()) {
                    case getFlight:
                        MessageFromServer messageFromServer = new ListFromServer(TypeMessage.getFlight, flights);
                        getFlight = new Gson().toJson(messageFromServer);
                        out.writeUTF(getFlight);
                        out.flush();
                        break;
                    case deleteFlight:
                        i = message.getId();
                        if (journal.isVariability() == false) {
                            flightsMap.remove(i);
                            flights=updateFlights(flightsMap);
                        } else {
                            out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                            out.flush();
                        }
                        Message message1 = new GeneralMessage(TypeMessage.deleteFlight, null, i);
                        getFlight = new Gson().toJson(message1);
                        out.writeUTF(getFlight);
                        out.flush();
                        Server.Update(message1,gson);
                        Server.setFlights(flightsMap);
                        break;
                    case addFlight:
                        Flight flightAdd = (Flight) message.getObject();
                        flightsMap.put(flightAdd.getId(), flightAdd);
                        flights=updateFlights(flightsMap);
                        Message message2 = new GeneralMessage(TypeMessage.addFlight, flightAdd, flightAdd.getId());
                        getFlight = gson.toJson(message2);
                        out.writeUTF(getFlight);
                        out.flush();
                        Server.Update(message2,gson);
                        Server.setFlights(flightsMap);
                        break;
                    case editFlight:
                        Flight flightEdit = (Flight) message.getObject();
                        if (flightEdit.isVariability() == false) {
                            flightEdit.isVariabilitytrue();
                            flightsMap.replace(flightEdit.getId(), flightEdit);
                        } else {
                            out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                        }
                        flights=updateFlights(flightsMap);
                        Message message3 = new GeneralMessage(TypeMessage.editFlight, flightEdit, flightEdit.getId());
                        getFlight = gson.toJson(message3);
                        out.writeUTF(getFlight);
                        out.flush();
                        Server.Update(message3,gson);
                        Server.setFlights(flightsMap);
                        break;
                    case quit:
                        in.close();
                        out.close();
                        Dialog.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Клиент закончил работу...");
        }
    }

    @Override
    public void Update(Message message,Gson gson) throws IOException {
        DataOutputStream out = new DataOutputStream(Dialog.getOutputStream());
        DataInputStream in = new DataInputStream(Dialog.getInputStream());
        String getFlight;
        try {
            getFlight = gson.toJson(message);
            out.writeUTF(getFlight);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Flight> updateFlights(Map<Integer, Flight> flightsMap) {
        List<Flight> flightsList = new ArrayList<Flight>();
        for (Map.Entry<Integer, Flight> fly : flightsMap.entrySet()) {
            flightsList.add(fly.getValue());
        }
        return flightsList;
    }
    //todo при добавлении перелета сделать обработку по Id
    // hashmap добавить для хранения перелетов -
    // блокировка элемента который используется
    // сервер перед тем как клиент редактирует принимает id
}
