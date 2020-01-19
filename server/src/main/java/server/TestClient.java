package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Airbus;
import general.Flight;
import general.Route;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestClient {
    public static void main(String[] args) {
        Route route = new Route("NewYork", "OldYork");
        Route route1 = new Route("Boston", "Omsk");
        Route route2 = new Route("Omsk", "Omsk");
        Date date = new Date();
        Flight flight = new Flight(1, Airbus.Airbius_A319, route, date, 30);
        Flight flight1 = new Flight(2, Airbus.Airbius_A321, route1, date, 56);
        Flight flight2 = new Flight(3, Airbus.Airbius_A300, route2, date, 90);
        List flights = new ArrayList<Flight>();
        flights.add(flight);
        flights.add(flight1);
        flights.add(flight2);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter =new FileWriter("JournalFlights");
            fileWriter.write(gson.toJson(flights));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            List<Flight> journal = new ArrayList<Flight>();
            ArrayList<Flight> flights1 = new ArrayList<Flight>();
            FileReader fileReader = new FileReader("JournalFlights");
            flights1 = gson.fromJson(fileReader,Flight.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    }
