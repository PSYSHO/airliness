package general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*import com.sun.org.apache.bcel.internal.util.BCELifier;*/

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Journal {
    List<Flight> journal = new ArrayList<Flight>();

    public Journal(){}

    public Map load(String path, Map map){
        Journal journal = new Journal();
        try {
            FileReader fileReader = new FileReader(path);
            journal=new Gson().fromJson(fileReader,Journal.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Flight flight:journal.journal){
            map.put(flight.getId(),flight);
        }
        return map;
    }

    public void save(String path,Map map){
        Journal journal=new Journal();
        for (Object flight : map.values()) {
            journal.journal.add((Flight) flight);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(gson.toJson(journal));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void create(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Journal journal = new Journal();
        Date date = new Date();
        Route route = new Route("Omsk","Omsk");
        Flight flight = new Flight(1,Airbus.Airbius_A321,route,date,50);
       // journal.journal.add(flight);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("journal.json");
            fileWriter.write(gson.toJson(journal));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
