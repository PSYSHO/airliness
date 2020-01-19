package general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Journal {
    List journal = new ArrayList<Flight>();
    public Journal(){}

    public List getJournal() {
        return journal;
    }

    public void setJournal(List journal) {
        this.journal = journal;
    }
    public Flight getFlight(int index){
        Flight  flight = (Flight) journal.get(index);
        return flight;
    }
    public int getSize(){
        int size  =  journal.size();
        return size;
    }
    public Map load(String path, Map map){
        Journal journal = new Journal();
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader("journal.json");
            journal = gson.fromJson(fileReader, Journal.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int k = journal.getSize();
        for (int i = 0; i < k; i++) {
            List list = journal.getJournal();
            map.put(i,list.get(i));
        }
        return map;
    }
    public void delete(int index,Map map){
        for(Object o:map.values()){
            if (o==journal.get(index)){
                journal.remove(index);
                map.remove(o);
            }
        }
    }
    public void save(String path,Map map){
        List flights = new ArrayList<Flight>();
        for (Object flight : map.values()) {
            flights.add((Flight) flight);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter("journal.json");
            fileWriter.write(gson.toJson(flights));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
