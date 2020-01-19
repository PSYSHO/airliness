package request;

import general.Flight;
import general.TypeMessage;

/**
 * Интерфейс с сообщением , с объектом и индексом.
 */
public interface Message {

    /**
     * Сообщение запроса
     */
    TypeMessage getMessage();

    /**
     * Метод изменяющий сообщение запроса
     *
     * @param message сообщение запроса
     */
    void setMessage(TypeMessage message);

    /**
     * Метод возвращающий объект запроса
     */
    Object getObject();

    /**
     * Метод изменяющий объект запроса.
     *
     * @param object объект запроса
     */
    void setObject(Object object);

    /**
     * Метод возвращающий индекс запроса.
     * Если в запросе не нужне индекс, возвращает значение -1.
     *
     * @return
     */
    int getId();

    /**
     * Метод изменяющий индекс запроса
     *
     * @param index индекс запроса
     */
    void setId(int index);
}
