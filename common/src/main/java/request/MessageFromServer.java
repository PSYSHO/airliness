package request;

import general.Flight;
import general.TypeMessage;

import java.util.List;

/**
 * Интерфейс сообщения со списком.
 */
public interface MessageFromServer {

    /**
     * Сообщение киенту от сервера.
     */
    TypeMessage getMessage();

    /**
     * Метод для изменения сообщения.
     */
    void setMessage(TypeMessage message);

    /**
     * Метод возвращаюзий список.
     */
    List<Flight> getList();

}
