package general;

/**
 * Класс со значениями возможных сообщений используемых в программе.
 */
public enum TypeMessage {
    getFlight("Все рейсы выведены на экран..."),
    deleteFlight(" удален..."),
    editFlight(" изменен..."),
    addFlight(" добавлен..."),
    cannotChage(" нельзя изменять в данный момент..."),
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
