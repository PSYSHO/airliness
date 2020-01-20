package general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*import com.sun.org.apache.bcel.internal.util.BCELifier;*/

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Journal {
    List<Flight> journal = new ArrayList<Flight>();

    public Journal(){}

    public List getJournal() {
        return journal;
    }

    public void setJournal(List<Flight> journal) {
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
        try {
            FileReader fileReader = new FileReader(path);
            journal=new Gson().fromJson(fileReader,Journal.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //int k = journal.getSize();
        /*for (int i = 0; i < k; i++) {
            List list = journal.getJournal();
            map.put(i,list.get(i));
        }*/
        for(Flight flight:journal.journal){
            map.put(flight.getId(),flight);
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
        Journal journal=new Journal();
        for (Object flight : map.values()) {
            journal.journal.add((Flight) flight);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter("journal.json");
            fileWriter.write(gson.toJson(journal));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
