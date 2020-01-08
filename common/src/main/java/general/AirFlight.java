package general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AirFlight {
    List journal = new ArrayList<Flight>();

    public AirFlight(){}

    public AirFlight(List journal) {
        this.journal=journal;
    }

    public AirFlight(ArrayList<Flight> flights) {
        this.journal = flights;
    }

    public List<Flight> getJournal() {
        return journal;
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

}
