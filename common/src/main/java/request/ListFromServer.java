package request;

import general.Flight;
import general.TypeMessage;

import java.util.List;

/**
 * Класс для отправки сообщения клиенту со списком рейсов
 * @author Kashkinov Sergey
 */
public class ListFromServer implements MessageFromServer {

    /**
     * Поле сообщения запроса
     */
    private TypeMessage message;

    /**
     * Поле списка запроса
     */
    private List<Flight> list;

    /**
     * Конструктор принимающий значения списка и сообщения.
     * @param message - сообщение.
     * @param list - список.
     */
    public ListFromServer(TypeMessage message, List<Flight> list){
        this.list=list;
        this.message=message;
    }

    /**
     * Метод возвращающий сообщение.
     */
    @Override
    public TypeMessage getMessage() {
        return message;
    }

    /**
     * Метод изменяюий сообщение.
     */
    @Override
    public void setMessage(TypeMessage message) {
        this.message=message;
    }

    /**
     * Метод возвращающий список.
     */
    @Override
    public List<Flight> getList() {
        return list;
    }
}
