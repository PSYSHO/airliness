package general;

public enum TypeMessage {
    getFlight("Get Flights"),
    deleteFlight("Delete Flight"),
    editFlight("Edit Flight"),
    addFlight("Add Flight");

    private String message;

    TypeMessage(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
