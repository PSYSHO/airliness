package request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.AirFlight;
import general.Flight;
import general.Route;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;

import java.io.IOException;

public class GeneralRequest implements Request {
    Request request;
    Gson gson;
    String jsonRequest;
    /**
     * Поле сообщения запроса
     */
    private String message;

    /**
     * Поле объекта запроса
     */
    private Flight object;

    /**
     * Поле индекса запроса
     */
    private int index;

    public GeneralRequest(String message, Flight object, int index){
        this.message=message;
        this.object=object;
        this.index=index;
    }

    public GeneralRequest(String message, AirFlight obj){}
    public GeneralRequest(String message, Flight obj) {
        this.message = message;
        this.object = obj;
    }

    public GeneralRequest() {

    }

    /**
     * Сообщение запроса
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Метод изменяющий сообщение запроса
     *
     * @param message сообщение запроса
     */
    @Override
    public void setMessage(String message) {
        this.message=message;
    }

    /**
     * Метод возвращающий объект запроса
     * Если в запросе не нужен объект для передачи серверу,
     * то передается объект по умолчанию.
     */
    @Override
    public Object getObject() {
        return object;
    }

    /**
     * Метод изменяющий объект запроса.
     *
     * @param object объект запроса
     */
    @Override
    public void setObject(Flight object) {
        this.object=object;
    }

    /**
     * Метод возвразвращающий индекс запроса.
     * Если в запросе не нужне индекс, возвращает значение -1.
     *
     * @return возвращает индекс
     */
    public int getIndex(){
        return index;
    }

    /**
     * Метод изменяющий индекс запроса
     *
     * @param index индекс запроса
     */
    @Override
    public void setIndex(int index) {
        this.index=index;
    }
    public String receivingRequest(String message, AirFlight obj) throws IOException {
        request=new GeneralRequest(message,obj);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GeneralRequest.class, new CustomConverter())
                .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                .registerTypeAdapter(Route.class, new CustomConverterRoute())
                .create();
        jsonRequest=gson.toJson(request);
        return jsonRequest;
    }
}
