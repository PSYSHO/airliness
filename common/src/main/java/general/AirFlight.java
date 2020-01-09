package general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AirFlight {
    int size;
    boolean tip = false ;
    List journal = new ArrayList<Flight>();

    public AirFlight(){}

    public AirFlight(List journal) {
        this.journal=journal;
        this.size = journal.size();
    }

    public AirFlight(ArrayList<Flight> flights) {
        this.journal = flights;
    }

    public List<Flight> getJournal() {
        return journal;
    }
    public Flight getFlight(int i){
       Flight flight = (Flight) journal.get(i);
       return flight;
    }

    public void setJournal(ArrayList<Flight> journal) {
        this.journal = journal;
    }

    public void add(Flight flight) {
        journal.add(flight);
    }

    public void remove(Flight flight) {
        journal.remove(flight);
    }

    public void removeAll(ArrayList<Flight> flights) {
        journal.addAll(flights);
    }

    public void remove(int i) {
        journal.remove(i);
    }
    public void refactor(int i,Flight flight){
        journal.set(i, flight);
    }
    public boolean busy(int i ){
       Flight flight = (Flight) journal.get(i);
       if (tip == flight.isVariability());
       return true;
    }
    public int getSize(){return journal.size();}

}
