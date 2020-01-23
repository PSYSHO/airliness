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

public class AirLinesServer implements ControlInterface{

    private int i = 0;
    private static Socket Dialog;
    private volatile Map<Integer, Flight> flightsMap = new HashMap<Integer, Flight>();
    private volatile List<Flight> flights = new ArrayList<Flight>();
    private DataOutputStream out;
    private DataInputStream in;
    private boolean flagBlock = false;
    private int intBlock = -1;
    private Thread thread;

    public AirLinesServer(Socket client, Map<Integer, Flight> map) {
        AirLinesServer.Dialog = client;
        flightsMap = map;
    }

    public void setIntBlock(int intBlock, boolean flagBlock) {
        this.intBlock = intBlock;
        this.flagBlock = flagBlock;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void initThread(Thread thread){
        this.thread=thread;
    }


    @Override
    public void run() {
        try {
            Message message;
            Flight journal = new Flight();
            flights = updateFlights(flightsMap);
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
                if(!thread.isInterrupted()) {
                    while(in.available()<1){
                        if(thread.isInterrupted()){
                            Message message1 = new GeneralMessage(TypeMessage.quit, null, i);
                            getFlight = new Gson().toJson(message1);
                            out.writeUTF(getFlight);
                            out.flush();
                            throw new InterruptedException();
                        }
                    }
                    json = in.readUTF();
                    message = gson.fromJson(json, GeneralMessage.class);
                    switch (message.getMessage()) {
                        case getFlight:
                            MessageFromServer messageFromServer = new ListFromServer(TypeMessage.getFlight, flights);
                            getFlight = new Gson().toJson(messageFromServer);
                            out.writeUTF(getFlight);
                            out.flush();
                            break;
                        case deleteFlight:
                            i = message.getId();
                            if (!flagBlock || i != intBlock) {
                                if (journal.isVariability() == false) {
                                    flightsMap.remove(i);
                                    flights = updateFlights(flightsMap);
                                } else {
                                    out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                                    out.flush();
                                }
                                Message message1 = new GeneralMessage(TypeMessage.deleteFlight, null, i);
                                getFlight = new Gson().toJson(message1);
                                out.writeUTF(getFlight);
                                out.flush();
                                Server.update(message1, gson);
                                Server.setFlights(flightsMap);
                            } else {
                                Message message1 = new GeneralMessage(TypeMessage.cannotChange, null, intBlock);
                                getFlight = new Gson().toJson(message1);
                                out.writeUTF(getFlight);
                                out.flush();
                            }
                            break;
                        case addFlight:
                            Flight flightAdd = (Flight) message.getObject();
                            flightsMap.put(flightAdd.getId(), flightAdd);
                            flights = updateFlights(flightsMap);
                            Message message2 = new GeneralMessage(TypeMessage.addFlight, flightAdd, flightAdd.getId());
                            getFlight = gson.toJson(message2);
                            out.writeUTF(getFlight);
                            out.flush();
                            Server.update(message2, gson);
                            Server.setFlights(flightsMap);
                            break;
                        case editFlight:
                            if (!flagBlock || i != intBlock) {
                                Flight flightEdit = (Flight) message.getObject();
                                if (flightEdit.isVariability() == false) {
                                    flightEdit.isVariabilitytrue();
                                    flightsMap.replace(flightEdit.getId(), flightEdit);
                                } else {
                                    out.writeUTF(String.valueOf(TypeMessage.objectIsBusy));
                                }
                                flights = updateFlights(flightsMap);
                                Message message3 = new GeneralMessage(TypeMessage.editFlight, flightEdit, flightEdit.getId());
                                getFlight = gson.toJson(message3);
                                out.writeUTF(getFlight);
                                out.flush();
                                Server.update(message3, gson);
                                Server.setFlights(flightsMap);
                                Server.unblockFlight(-1, this);
                            } else {
                                Message message1 = new GeneralMessage(TypeMessage.cannotChange, null, intBlock);
                                getFlight = new Gson().toJson(message1);
                                out.writeUTF(getFlight);
                                out.flush();
                            }
                            break;
                        case blockElement:
                            Server.blockFlight(message.getId(), this);
                            System.out.println(message.getId());
                            break;
                        case quit:
                            in.close();
                            out.close();
                            Dialog.close();
                            System.out.println("Отдельный сервер завершил работу с клиентом..");
                            break;
                        default:
                            break;
                    }
                } else {
                    Message message1 = new GeneralMessage(TypeMessage.quit, null, i);
                    getFlight = new Gson().toJson(message1);
                    out.writeUTF(getFlight);
                    out.flush();
                    throw new InterruptedException();
                }
            }
        } catch (IOException | InterruptedException e) {
            if(e instanceof IOException) {
                System.out.println("Клиент закончил работу...");

            } else {
                System.out.println("Поток работающий с клиентом закончил работу...");
            }
        }
    }

    private List<Flight> updateFlights(Map<Integer, Flight> flightsMap) {
        List<Flight> flightsList = new ArrayList<Flight>();
        for (Map.Entry<Integer, Flight> fly : flightsMap.entrySet()) {
            flightsList.add(fly.getValue());
        }
        return flightsList;
    }
}
