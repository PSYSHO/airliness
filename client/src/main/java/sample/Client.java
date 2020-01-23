package sample;


import general.Flight;
import general.Route;
import general.TypeMessage;
import gsonConverter.CustomConverter;
import gsonConverter.CustomConverterFlight;
import gsonConverter.CustomConverterRoute;
import controllers.ThreadAlert;
import javafx.application.Platform;
import request.GeneralMessage;
import request.ListFromServer;
import request.Message;

import java.io.*;
import java.net.Socket;

import com.google.gson.*;
import request.MessageFromServer;
import controllers.MainSceneController;

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
        fil = new FileInputStream("config.properties");
        property = new Properties();
        property.load(fil);
        String host = property.getProperty("may.host");
        String port = property.getProperty("may.port");
        socket = new Socket(host, Integer.parseInt(port));
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.controller = controller;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GeneralMessage.class, new CustomConverter())
                .registerTypeAdapter(Flight.class, new CustomConverterFlight())
                .registerTypeAdapter(Route.class, new CustomConverterRoute())
                .create();
        look = new ReentrantLock();

        thread = new Thread(this::waitingMessage);
        thread.start();
    }

    /**
     * Метод создающий запрос, создающий Gson объект для серелиализаци и десериализации данных
     *      * и переносящий запрос в форму строки
     *
     * @param message - сообщение от клиента серверу, сообщающее что нужно делать.
     * @param obj     - объект передающийся для изменеия списка на сервере.
     * @param id      - id этого объекта.
     */
    public void receivingMessage(TypeMessage message, Flight obj, int id) throws IOException {
        this.message = new GeneralMessage(message, obj, id);
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
                                controller.listUpdate(message.getObject(), message.getMessage(), message.getId());
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