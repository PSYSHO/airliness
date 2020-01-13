package general;

import java.util.ArrayList;

public enum TravelCities {
    Samara("Самара"),
    Saint_Petersburg("Санкт-Петербург"),
    Saratov("Саратов"),
    Moscow("Москва"),
    Kazan("Казань"),
    Tula("Тула"),
    Omsk("Омск");

    private String nameTown;

    TravelCities(String nameTown){
        this.nameTown=nameTown;
    }

    public String getNameTown(){
        return nameTown;
    }


}
