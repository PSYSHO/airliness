package sample;

import general.AirFlight;
import general.Flight;
import general.Route;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import interfaceController.MainSceneController;
import request.GeneralRequest;
import request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Socket;

import com.google.gson.*;
import interfaceController.MainSceneController;

import java.util.ArrayList;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Request request;
    private Gson gson;
    private String jsonRequest;
    private MainSceneController controller;

    /**
     * В конструкторе происходит инициализация сокета
     * и его потоков ввода/вывода
     */
    public Client(MainSceneController controller) throws IOException {
        socket = new Socket("localhost", 8000);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.controller=controller;
        System.out.println("Init Client");
    }

    /**
     * Метод создающий запрос, создающий Gson объект для серелиализаци и десериализации данных
     * и переносящий запрос в форму строки
     *
     */
    public void receivingRequest(String message, Flight obj,int index) throws IOException {
        request=new GeneralRequest(message,obj ,index);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GeneralRequest.class, new CustomConverter())
                .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                .registerTypeAdapter(Route.class, new CustomConverterRoute())
                .create();
        jsonRequest=gson.toJson(request);
    }

    /**
     * Заносим в поток ввод серелизованный обект интерфейса Request
     * и по потоку вывода передаем серверу
     */
    public void getAllFlight(String message, Flight obj,int index) throws IOException {
        receivingRequest(message, obj, index);
        try {
            out.writeUTF(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения");
        }
        try {
            Gson gsonList=new Gson();
            Type type = new ListParameterizedType(Flight.class);
            String str=in.readUTF();
            AirFlight list = new AirFlight(gsonList.fromJson(str, type));

            controller.listFlights(list.getJournal());
        } catch(IOException e){
            System.out.println("Ошибка приема сообщения");
        }
    }

    /**
     * Отправляет запрос на удаление на сервере элемента под определенным индексом
     *
     * @param message сообщение серверу по обработке данных
     * @param obj объкт передаваемый для взаимодействия с сервером
     * @param index номер элемента, который нужно удалить
     */
    public void deleteFlight(String message, Flight obj,int index) throws IOException {
        receivingRequest(message, obj, index);
        try {
            out.writeUTF(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения");
        }
        try {
            String getFlights = in.readUTF();
            ArrayList list = new ArrayList<Flight>();
            list = new Gson().fromJson(getFlights, ArrayList.class);
            System.out.println("Ошибка приема сообщения");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Перед закрытием приложения будут закрыты все потоки и соккет
     */
    public void stop() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    public void edit(Flight obj,String message,int index) throws IOException {
            receivingRequest(message, obj, index);
        try{
            out.writeUTF(jsonRequest);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Type type = new ListParameterizedType(Flight.class);
            String str=in.readUTF();
            AirFlight list = new AirFlight(new Gson().fromJson(str, type));
            controller.listFlights(list.getJournal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String message,Flight obj,int index) throws IOException {
        receivingRequest(message, obj, index);
        try{
            out.writeUTF(jsonRequest);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Type type = new ListParameterizedType(Flight.class);
            String str=in.readUTF();
            AirFlight list = new AirFlight(new Gson().fromJson(str, type));
            controller.listFlights(list.getJournal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Класс реализующий десериализацию списка элементов в объект
     */
    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        // implement equals method too! (as per javadoc)
    }
}

