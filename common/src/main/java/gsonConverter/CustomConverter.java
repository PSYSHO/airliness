package gsonConverter;

import com.google.gson.*;
import general.Flight;
import general.TypeMessage;
import request.GeneralMessage;
import request.Message;

/**
 * Класс для сериализиции и десериалиции обекта типа Message
 * @author Kashkinov Sergey
 */
public class CustomConverter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message src, java.lang.reflect.Type type,
                                 JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("message", src.getMessage().name());
        object.add("object", context.serialize(src.getObject(), Flight.class));
        object.addProperty("index", src.getId());
        return object;
    }

    @Override
    public Message deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Message message = new GeneralMessage();
        message.setMessage(TypeMessage.valueOf(jsonObject.get("message").getAsString()));
        message.setObject(context.deserialize(jsonObject.get("object"), Flight.class));
        message.setId(jsonObject.get("index").getAsInt());
        return message;
    }
}
