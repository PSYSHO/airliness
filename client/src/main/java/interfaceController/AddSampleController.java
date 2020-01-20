package interfaceController;

import general.Airbus;
import general.Flight;
import general.Route;
import general.TravelCities;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
/*import javax.swing.text.StringContent;
import javax.xml.soap.Node;
import java.awt.*;
import java.text.DateFormat;*/
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;*/

/**
 * Класс-контролер отвечающий за окно добавления нового элемента.
 *
 * @author Kashkinov Sergeu
 */
public class AddSampleController {
    private DateTimeFormatter formatter;
    private Stage stage;
    private Flight flight;
    private SimpleDateFormat dateFormat;
    private List<Flight> oldFlights;

    /**
     * переменная флаг, меняется на true, только в том случае когда создалась правильный объект Flight
     */
    private boolean flag = false;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<Airbus> comboAirbus;

    @FXML
    private TextField id;

    @FXML
    private TextField time;

    @FXML
    private TextField travelTimeMinutes;

    @FXML
    private ComboBox<String> from;

    @FXML
    private ComboBox<String> to;

    /**
     * Метод возвращающий созданный объект.
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Метод возвращающий значение списка String, со значением всех возможных городов отправления.
     *
     * @return список гороов.
     */
    private ArrayList<String> getMasNameTown() {
        ArrayList<String> masNameTown = new ArrayList<String>();
        for (TravelCities element : TravelCities.values()) {
            masNameTown.add(element.getNameTown());
        }
        return masNameTown;
    }

    /**
     * Инициализирует ComboBox элементами типа Airbus,
     * инициализирует formatter для нужного мне перевода из
     * LocalDate в тип String, инициализирует dateFormat для
     * нужного мне вывода строки на экране.
     */
    @FXML
    private void initialize() {
        comboAirbus.setItems(FXCollections.observableArrayList(Airbus.values()));
        from.setItems(FXCollections.observableArrayList(getMasNameTown()));
        from.setEditable(true);
        to.setItems(FXCollections.observableArrayList(getMasNameTown()));
        to.setEditable(true);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        StringConverter<Date> convertDate = new StringConverter<Date>() {
            @Override
            public String toString(Date date) {
                if (date != null) {
                    return dateFormat.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public Date fromString(String stringDate) {
                if (stringDate != null && !stringDate.isEmpty()) {
                    try {
                        return dateFormat.parse(stringDate);
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                } else {
                    return null;
                }
                return null;
            }
        };
    }

    /**
     * Метод принимающий значения каркаса и исходного списка.
     *
     * @param stage      - каркас.
     * @param oldFlights - исходный список.
     */
    public void setStage(Stage stage, List<Flight> oldFlights) {
        this.stage = stage;
        this.oldFlights = oldFlights;
    }

    /**
     * Метод вызывающий закрытие окна.
     */
    public void cancel(javafx.event.ActionEvent actionEvent) {
        stage.close();
    }

    /**
     * Метод создания нового объекта Flight
     *
     * @param actionEvent реакция на нажитие
     * @throws ParseException ошибка соединения
     */
    @FXML
    public void add(ActionEvent actionEvent) throws ParseException {
        flight = new Flight();
        if (inputCheck()) {
            flight.setId(Integer.parseInt(id.getText()));
            flight.setIdAirbus(comboAirbus.getValue());
            String dateString = datePicker.getValue().format(formatter) + " " + time.getText();
            flight.setDeparture(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateString));
            flight.setRoute(new Route(from.getValue(), to.getValue()));
            flight.setTravelTime(Integer.parseInt(travelTimeMinutes.getText()));
            /*if(!from.getEditor().getText().equals("")){
                fromItems.add(from.getEditor().getText());
                System.out.println(from.getEditor().getText());
            }*/
            stage.close();
            flag = true;
        }
    }

    /**
     * Метод проверки вводимых данных
     *
     * @return если проверка прошла успешно, то вернет true
     */
    private boolean inputCheck() {
        String errorMessage = "";
        if ((id.getText() == null) || (id.getText().length() == 0)) {
            errorMessage += "Незаполнено поле ID";
        } else {
            try {
                if (searchId(Integer.parseInt(id.getText()))) {
                    errorMessage += "Значение поля Id повторяется";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Неверный формат записи поля Id";
            }
        }

        if (comboAirbus.getValue() == null) {
            errorMessage += "Невыбрано значение поля Airbus";
        }
        if ((datePicker.getValue() == null) || ((time.getText() == null) || (time.getText().length() == 0))) {
            errorMessage += "Невыбрано значение поля даты или времени";
        } else {
            try {
                String dateTimeString = datePicker.getValue().format(formatter) + " " + time.getText();
                Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateTimeString);
                String[] timeString = time.getText().split(":");
                int hour = Integer.parseInt(timeString[0]);
                int minute = Integer.parseInt(timeString[1]);
                if (!((hour >= 0 && hour <= 24) && (minute >= 0 && minute <= 60)))
                    errorMessage += "Неверно записано время";
                String[] dateString = datePicker.getValue().format(formatter).split("\\.");
                int day = Integer.parseInt(dateString[0]);
                int mouth = Integer.parseInt(dateString[1]);
                int year = Integer.parseInt(dateString[2]);
                if (!((day >= 0 && day <= 31) && (mouth >= 1 && mouth <= 12) && (year >= 1970 && year <= 3000)))
                    errorMessage += "Неверно записано дата";
            } catch (ParseException e) {
                errorMessage += "Неверный формат записи поля времени";
            }
        }
        if (from.getValue() == null) {
            errorMessage += "Невыбран пункт отправления";
        }
        if (to.getValue() == null) {
            errorMessage += "Невыбран пункт назначения";
        }
        if (from.getValue().equals(to.getValue())) {
            errorMessage += "Пункты отправления и назначения не должны повторяться";
        }
        if ((travelTimeMinutes.getText() == null) || (travelTimeMinutes.getText().length() == 0)) {
            errorMessage += "Невведено значение в поле продолжительность полета";
        } else {
            try {
                Integer.parseInt(travelTimeMinutes.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Неверный формат поля продолжительность полета";
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Неверно введеные значения");
            alert.setHeaderText("Пожалуйста, повторите ввод еще раз");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }

    }

    /**
     * Поиск повторяющийхс уникальных ключей
     */
    public boolean searchId(int id) {
        for (Flight flight : oldFlights) {
            if (flight.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод вызываемый контроллером, чтобы уточнить добавлять или нет созданный объект
     *
     * @return возвращает значение flag
     */
    public boolean isOnClick() {
        return flag;
    }
}
