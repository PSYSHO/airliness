package general;

public class Route {
    private String pointOfDeparture;
    private String pointOfArrival;

    public Route() {
    }

    public Route(String pointOfDeparture, String pointOfArrival) {
        this.pointOfDeparture = pointOfDeparture;
        this.pointOfArrival = pointOfArrival;
    }

    public String getPointOfDeparture() {
        return pointOfDeparture;
    }

    public String getPointOfArrival() {
        return pointOfArrival;
    }

    public void setPointOfArrival(String pointOfArrival) {
        this.pointOfArrival = pointOfArrival;
    }

    public void setPointOfDeparture(String pointOfDeparture) {
        this.pointOfDeparture = pointOfDeparture;
    }

    public  String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pointOfDeparture).append(" | ").append(pointOfArrival);
        return stringBuilder.toString();
    }
}
