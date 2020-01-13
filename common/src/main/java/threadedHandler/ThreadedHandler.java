package threadedHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import general.Airbus;
import general.Flight;
import general.Route;
import request.GeneralMessage;
import request.Message;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadedHandler implements Runnable {
    private Socket incoming;
    private List<Flight> flights=getFlights();


    public ThreadedHandler(Socket incoming) {
        this.incoming = incoming;
    }

    @Override
    public void run() {
        try (DataOutputStream write = new DataOutputStream(
                                                incoming.getOutputStream());
             DataInputStream read = new DataInputStream(
                                                incoming.getInputStream())
        ) {
            while (true) {
                while (read.available() == 0) {
                }
                String json = "";
                while (read.available() > 0) {
                    try {
                        json = read.readUTF();
                    } catch (IOException e) {
                        System.out.println("Ошибка приема сообщения");
                    }
                }
                String getFlightsJson;
                // собственный сериализатор десериализатор
                Gson  gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(GeneralMessage.class, new CustomConverter())
                        .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                        .registerTypeAdapter(Route.class, new CustomConverterRoute())
                        .create();
                //десериализуем запрос
                Message message = gson.fromJson(json, GeneralMessage.class);
                switch (message.getMessage()) {
                    case getFlight:
                        //сериализуем список рейсов и отправляем клиенту
                        getFlightsJson = new Gson().toJson(flights);
                        write.writeUTF(getFlightsJson);
                        write.flush();
                        break;

                    case deleteFlight:
                        //получаем из запроса индекс элемента на удаление
                        int i= message.getIndex();
                        //удаяем его из списка
                        for(int j=0;j<flights.size(); j++) {
                            if(flights.get(j).getId()==i){
                                flights.remove(j);
                                break;
                            }
                        }
                        //получившийся список сериализуем и отправляем клиенту
                        getFlightsJson = new Gson().toJson(flights);
                        write.writeUTF(getFlightsJson);
                        write.flush();
                        break;
                    case editFlight:
                        //получаем объект на изменение
                        Flight flightEdit= message.getObject();
                        //получаем его индекс
                        int indexEdit= message.getIndex();
                        //сортируем список чтобы индекс не отличался от сортированного списка клиента
                        Collections.sort(flights);
                        //изменяем объект списка
                        for(int j=0;j<flights.size(); j++) {
                            if(flights.get(j).getId()==indexEdit){
                                flights.set(j,flightEdit);
                                break;
                            }
                        }
                        //flights.set(indexEdit,flightEdit);
                        //сериализуем список и отправляем клиенту
                        getFlightsJson = new Gson().toJson(flights);
                        write.writeUTF(getFlightsJson);
                        write.flush();
                        break;
                    case addFlight:
                        Flight flightAdd=(Flight) message.getObject();
                        flights.add(flightAdd);
                        Collections.sort(flights);
                        getFlightsJson = new Gson().toJson(flights);
                        write.writeUTF(getFlightsJson);
                        write.flush();
                        break;
                    default:
                        System.out.println("Нет такого сообщения");
                        break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * создаем тестовый список для проверки работы программы
     *
     * @return возвращает список ArrayList типа с элементами типа Flight
     */
    public ArrayList<Flight> getFlights() {
        GregorianCalendar calendar1 = new GregorianCalendar(2019, 11, 05, 16, 00);
        GregorianCalendar calendar2 = new GregorianCalendar(2019, 11, 05, 16, 01);
        GregorianCalendar calendar3 = new GregorianCalendar(2019, 12, 05, 16, 00);
        Date date1 = calendar1.getTime();
        Date date2 = calendar2.getTime();
        Date date3 = calendar3.getTime();
        ArrayList<Flight> list = new ArrayList<Flight>();
        Flight flight1 = new Flight(1, Airbus.Airbius_A321, new Route("Saratov", "Samara"), date1, 100);
        Flight flight2 = new Flight(2, Airbus.Airbius_A300, new Route("Tula", "Samara"), date3, 100);
        Flight flight3 = new Flight(3, Airbus.Airbius_A300, new Route("Omsk", "Samara"), date2, 100);
        list.add(flight1);
        list.add(flight2);
        list.add(flight3);
        return list;
    }
}
