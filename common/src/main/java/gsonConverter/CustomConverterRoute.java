package gsonConverter;

import com.google.gson.*;
import general.Flight;
import general.Route;

import java.lang.reflect.Type;

/**
 * Класс для сериализиции и десериалиции обекта типа Route
 * @author Kashkinov Sergey
 */
public class CustomConverterRoute implements JsonSerializer<Route>, JsonDeserializer<Route> {

    @Override
    public Route deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject=json.getAsJsonObject();
        Route route=new Route();
        route.setPointOfDeparture(jsonObject.get("from").getAsString());
        route.setPointOfArrival(jsonObject.get("to").getAsString());
        return route;
    }

    @Override
    public JsonElement serialize(Route route, Type type, JsonSerializationContext context) {
        JsonObject result=new JsonObject();
        result.addProperty("from",route.getPointOfDeparture());
        result.addProperty("to",route.getPointOfArrival());
        return  result;
    }
}

