package gsonConverter;

import com.google.gson.*;
import general.Airbus;
import general.Flight;
import general.Route;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Класс для сериализиции и десериалиции обекта типа Flight
 * @author Kashkinov Sergey
 */
public class CustomConverterFlight implements JsonSerializer<Flight>, JsonDeserializer<Flight> {

    /**
     * Дессериализия JsonObject объекта в объект типа Flight
     *
     * @param json - объект json
     * @param type - тип вкоторый преобразуем
     * @param context - контекст испозьзования
     * @return возвращиет объект типа Flight
     */
    @Override
    public Flight deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        JsonObject jsonObject=json.getAsJsonObject();
        Flight flight=new Flight();
        flight.setId(jsonObject.get("id").getAsInt());
        flight.setIdAirbus(Airbus.valueOf(jsonObject.get("airbus").getAsString()));
        flight.setDeparture(new Date(jsonObject.get("departure").getAsLong()));
        flight.setRoute(context.deserialize(jsonObject.get("route"), Route.class));
        flight.setTravelTime(jsonObject.get("travelTimeMinutes").getAsInt());
        return flight;
    }

    /**
     * Сериализаця Flight объекта в JsonObject
     *
     * @param flight объект который сериализуется
     * @param type тип сериализуемого объекта
     * @param context с его помощью, мы можим сериализовать не примитивные типы
     * @return возвращает объект типа JsonObject
     */
    @Override
    public JsonElement serialize(Flight flight, Type type, JsonSerializationContext context) {
        JsonObject result=new JsonObject();
        if (flight != null) {
            result.addProperty("id", flight.getId());
            result.addProperty("airbus", flight.getIdAirbus().name());
            result.addProperty("departure", flight.getDeparture().getTime());
            result.add("route", context.serialize(flight.getRoute()));
            result.addProperty("travelTimeMinutes", flight.getTravelTime());
        } else {
            result.addProperty("id", "");
            result.addProperty("airbus", "");
            result.addProperty("departure", "");
            result.add("route",null);
            result.addProperty("travelTimeMinutes", "");
        }
        return  result;
    }
}
