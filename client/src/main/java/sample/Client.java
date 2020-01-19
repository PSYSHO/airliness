package sample;


import general.Flight;
import general.Route;
import general.TypeMessage;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import interfaceController.ThreadAlert;
import javafx.application.Platform;
import request.GeneralMessage;
import request.ListFromServer;
import request.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import com.google.gson.*;
import request.MessageFromServer;
import interfaceController.MainSceneController;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс выполняющий функцию клиента.
 * Соединяется по порту с сервером, передают и принимает сообщения с данными от него.
 *
 * @author Kashkinov Sergeu
 */
public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Message message;
    private Gson gson;
    private String jsonRequest;
    private MainSceneController controller;
    private boolean threadStop = false;
    private Thread thread;
    private ReentrantLock look;
    private Properties property;
    private FileInputStream fil;
    private MessageFromServer messageFromServer;

    /**
     * В конструкторе происходит инициализация сокета
     * и его потоков ввода/вывода
     *
     * @param controller - коттролер главного окна интерфейса.
     */
    public Client(MainSceneController controller) throws IOException {
        fil = new FileInputStream("common/src/main/java/general/config.properties");
        property = new Properties();
        property.load(fil);
        String host = property.getProperty("may.host");
        String port = property.getProperty("may.port");
        socket = new Socket(host, Integer.parseInt(port));
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.controller = controller;
        System.out.println("Init Client");
        look = new ReentrantLock();
        thread = new Thread(new ThreadAlert(this));
        thread.start();
    }

    /**
     * Метод создающий запрос, создающий Gson объект для серелиализаци и десериализации данных
     * и переносящий запрос в форму строки
     *
     * @param message - сообщение от клиента серверу, сообщающее что нужно делать.
     * @param obj     - объект передающийся для изменеия списка на сервере.
     * @param id      - id этого объекта.
     */
    public void receivingMessage(TypeMessage message, Flight obj, int id) throws IOException {
        this.message = new GeneralMessage(message, obj, id);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GeneralMessage.class, new CustomConverter())
                .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                .registerTypeAdapter(Route.class, new CustomConverterRoute())
                .create();
        jsonRequest = gson.toJson(this.message);
        out.writeUTF(jsonRequest);
        out.flush();
    }

    /**
     * Перед закрытием приложения будут закрыты все потоки и соккет
     */
    public void stop() throws IOException {
        threadStop = true;
        out.close();
        in.close();
        socket.close();
    }

    /**
     * Метод ожидающий сообщения от сервера
     */
    public void waitingMessage() {
        while (!threadStop) {
            try {
                if (in.available() > 0) {
                    look.lock();
                    String incomingMessage = in.readUTF();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //находит в строке сообщение, если getFlight, то выполняет прием списка с рейсами, если другой, то в зависимости от сообщения выполняются определенные действия
                            String messageGetFlight = incomingMessage.substring(12, 21);
                            if (messageGetFlight.equals(TypeMessage.getFlight.name())) {
                                messageFromServer = new Gson().fromJson(incomingMessage, ListFromServer.class);
                                controller.listFlights(messageFromServer.getList(), TypeMessage.getFlight.getDescription());
                            } else {
                                message = gson.fromJson(incomingMessage, GeneralMessage.class);
                                controller.listUpdate(message.getObject(), message.getMessage(), message.getIndex());
                            }
                        }
                    });
                    look.unlock();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

 /**
 * Заносим в поток ввод серелизованный обект интерфейса Request
 * и по потоку вывода передаем серверу
 * <p>
 * Отправляет запрос на удаление на сервере элемента под определенным индексом
 *
 * @param message сообщение серверу по обработке данных
 * @param obj объкт передаваемый для взаимодействия с сервером
 * @param index номер элемента, который нужно удалить
 * <p>
 * Класс реализующий десериализацию списка элементов в объект
 *//*
    public void getAllFlight(TypeMessage message, Flight obj, int index) throws IOException {
        //this.index=index;
        receivingMessage(message, obj, index);
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

            controller.listFlights(list.getJournal(), "Все рейсы выведены успешно...");
        } catch(IOException e){
            System.out.println("Ошибка приема сообщения");
        }
    }

    *//**
 * Отправляет запрос на удаление на сервере элемента под определенным индексом
 *
 * @param message сообщение серверу по обработке данных
 * @param obj объкт передаваемый для взаимодействия с сервером
 * @param index номер элемента, который нужно удалить
 *//*
    public void deleteFlight(TypeMessage message, Flight obj,int index) throws IOException {
        receivingMessage(message, obj, index);
        try {
            out.writeUTF(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения об удалении");
        }
        try {
            Type type = new ListParameterizedType(Flight.class);
            String str=in.readUTF();
            AirFlight list = new AirFlight(new Gson().fromJson(str, type));

            controller.listFlights(list.getJournal(), "Рейс под id: "+index+" удален успешно...");
        } catch (IOException e) {
            System.out.println("Ошибка приема сообщения об удалении");
        }
    }*/

/*public void edit(Flight obj,TypeMessage message,int index) throws IOException {
            receivingMessage(message, obj, index);
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
            controller.listFlights(list.getJournal(),"Рейс под id: "+index+" изменен успешно...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(TypeMessage message,Flight obj,int index) throws IOException {
        receivingMessage(message, obj, index);
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
            controller.listFlights(list.getJournal(),"Рейс под id: "+index+" добавлен успешно...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
/**
 * Класс реализующий десериализацию списка элементов в объект
 *//*
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
    }*/