package request;

import general.Flight;
import general.TypeMessage;

public class GeneralMessage implements Message {
    /**
     * Поле сообщения запроса
     */
    private TypeMessage message;

    /**
     * Поле объекта запроса
     */
    private Object object;

    /**
     * Поле индекса запроса
     */
    private int index;

    public GeneralMessage(TypeMessage message, Object object, int index){
        this.message=message;
        this.object=object;
        this.index=index;
    }

    public GeneralMessage(){}

    /**
     * Сообщение запроса
     */
    @Override
    public TypeMessage getMessage() {
        return message;
    }

    /**
     * Метод изменяющий сообщение запроса
     *
     * @param message сообщение запроса
     */
    @Override
    public void setMessage(TypeMessage message) {
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
    public void setObject(Object object) {
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
}
