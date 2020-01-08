package gsonConverter;

import com.google.gson.*;
import general.Flight;
import request.GeneralRequest;
import request.Request;

public class CustomConverter implements JsonSerializer<Request>, JsonDeserializer<Request> {
    public JsonElement serialize(Request src, java.lang.reflect.Type type,
                                 JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("message", src.getMessage());
        object.add("object", context.serialize(src.getObject(), Flight.class));
        object.addProperty("index", src.getIndex());
        return object;
    }

    @Override
    public Request deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Request request = new GeneralRequest();
        request.setMessage(jsonObject.get("message").getAsString());
        request.setObject(context.deserialize(jsonObject.get("object"), Flight.class));
        request.setIndex(jsonObject.get("index").getAsInt());
        return request;
    }
}
