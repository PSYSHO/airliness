package general;

/**
 * Класс со значениями возможных сообщений используемых в программе.
 */
public enum TypeMessage {
    getFlight("Все рейсы выведены на экран..."),
    deleteFlight(" удален..."),
    editFlight(" изменен..."),
    addFlight(" добавлен..."),
    blockElement("блокировать элемент для изменения"),
    unblockElement("разблокировать ля изменения данный элемент"),
    cannotChange(" нельзя изменять в данный момент..."),
    objectIsBusy(""),
    update(""),
    quit("");

    private String description;

    TypeMessage(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }
}
