package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Flight;
import general.Journal;
import general.TypeMessage;
import request.GeneralMessage;
import request.Message;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class Server {
    private static Map<Integer, Flight> flightMap;
    private static List<AirLinesServer> clientList;
    private static  List<Thread> threadList;

    public Server() {
    }

    public static void setFlights(Map<Integer, Flight> flightMapUpdate) {
        flightMap = flightMapUpdate;
    }

    public static Map<Integer, Flight> getFlights() {
        return flightMap;
    }

    public static void main(String[] args) throws IOException {
        int i=0;
        Journal journal = new Journal();
        flightMap = new HashMap<Integer, Flight>();
        threadList=new ArrayList<Thread>();
        FileInputStream fil = new FileInputStream("config.properties");
        Properties property = new Properties();
        property.load(fil);
        String port = property.getProperty("may.port");
        String path = property.getProperty("may.path");
        File file = new File(path);
        if (file.exists()) ;
        else {
            journal.create();
        }
        journal.load(path, flightMap);
        clientList = new ArrayList<AirLinesServer>();
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Thread waitingExit = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean flag = true;
                    while (flag) {
                        try {
                            if (br.readLine().equalsIgnoreCase("quit")) {
                                System.out.println("Main server exiting...");
                                journal.save(path, flightMap);
                                flag = false;
                                for (Thread thread : threadList) {
                                    thread.interrupt();
                                }
                                server.close();
                            } else {
                                System.out.println("Неизвестная команда...");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            waitingExit.start();
            System.out.println("Сервер подключен...");
            while (!server.isClosed()) {
                try {
                    System.out.println("Ожидает клиента...");
                    Socket socketClient = server.accept();
                    AirLinesServer client = new AirLinesServer(socketClient, flightMap);
                    Thread clientThread = new Thread(client);
                    client.initThread(clientThread);
                    clientThread.start();
                    threadList.add(clientThread);
                    clientList.add(client);
                    System.out.println("Новый клиент подключен...");
                } finally {
                    System.out.println("Новый клиент под номером "+i+" подключен...");
                }
            }

        } catch (SocketException e){
            System.out.println("Сервер завершает работу...");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Cервер закончил свою работу...");
    }

    public static void update(Message message, Gson gson) throws IOException {
        for (AirLinesServer client : clientList) {
            String getFlight;
            try {
                getFlight = gson.toJson(message);
                client.getOut().writeUTF(getFlight);
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void blockFlight(int id,AirLinesServer clientNoBlock){
        for (AirLinesServer client : clientList) {
            if(client.equals(clientNoBlock));
            else {
                client.setIntBlock(id,true);
            }
        }
    }

    public static void unblockFlight(int id,AirLinesServer clientNoBlock){
        for (AirLinesServer client : clientList) {
            if(client.equals(clientNoBlock));
            else {
                client.setIntBlock(id,false);
            }
        }
    }
}

