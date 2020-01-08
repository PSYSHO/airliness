package general;


import java.util.Date;

public class Flight implements Comparable<Flight> {
    private int id;
    private Airbus idAirbus;
    private Route route;
    private Date departure;
    private  int travelTimeMinutes;
    private boolean variability = false;



    public Flight() {
        id=0;
        idAirbus=Airbus.NuN;
        route=new Route("","");
        departure=new Date(1000L);
        travelTimeMinutes=0;
    }

    public Flight(int id, Airbus idAirbus, Route route, Date departure, int travelTimeMinutes) {
        this.id = id;
        this.idAirbus = idAirbus;
        this.route = route;
        this.departure = departure;
        this.travelTimeMinutes = travelTimeMinutes;
    }

    public boolean isVariability() {
        return variability;
    }

    public boolean isVariabilitytrue() {
        return variability = true;
    }

    public boolean isVariabilityfalse() {
        return variability = false;
    }

    public int getId() {
        return id;
    }

    public Date getDeparture() {
        return departure;
    }

    public Route getRoute() {
        return route;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public int getTravelTime() {
        return travelTimeMinutes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setTravelTime(int travelTime) {
        this.travelTimeMinutes = travelTime;
    }

    public Airbus getIdAirbus() {
        return idAirbus;
    }

    public void setIdAirbus(Airbus idAirbus) {
        this.idAirbus = idAirbus;
    }


    @Override
    public int compareTo(Flight element) {
        return departure.compareTo(element.departure);
    }
}
