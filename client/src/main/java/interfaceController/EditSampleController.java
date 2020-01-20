package interfaceController;


import general.Airbus;
import general.Flight;
import general.Route;
import general.TravelCities;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс-контроллер окна изменения элемента.
 *
 * @author Kashkinov Sergeu
 */
public class EditSampleController {
    private Stage stage;
    private Flight flight;
    private DateTimeFormatter formatter;
    private SimpleDateFormat dateFormat;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label id;

    @FXML
    private ComboBox<Airbus> comboAirbus;

    @FXML
    private TextField time;

    @FXML
    private TextField travelTimeMinutes;

    @FXML
    private ComboBox<String> from;

    @FXML
    private ComboBox<String> to;

    /**
     * Метод возвращающий значение списка String, со значением всех возможных городов отправления.
     *
     * @return список гороов.
     */
    private List<String> getMasNameTown() {
        List<String> masNameTown = new ArrayList<String>();
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
     * Метод принимающий значения каркаса.
     *
     * @param stage - каркас.
     */
    public void setEditStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Выводит первичные значения объекта, выбранного для редактирования.
     *
     * @param flight объект выбранный для редактирования
     */
    public void setEditFlight(Flight flight) {
        this.flight = flight;
        id.setText(Integer.toString(flight.getId()));
        comboAirbus.setValue(flight.getIdAirbus());
        String[] dateString = converterDateToString(flight.getDeparture())[0].split("\\.");
        int day = Integer.parseInt(dateString[0]);
        int mouth = Integer.parseInt(dateString[1]);
        int year = Integer.parseInt(dateString[2]);
        datePicker.setValue(LocalDate.of(year, mouth, day));
        time.setText(converterDateToString(flight.getDeparture())[1]);
        from.setValue(flight.getRoute().getPointOfDeparture());
        to.setValue(flight.getRoute().getPointOfArrival());
        travelTimeMinutes.setText(Integer.toString(flight.getTravelTime()));
    }

    /**
     * Метод преобразующий дату в массив String.
     *
     * @param date - дата.
     * @return возвращает массив из двух значений, первое - дата, второе - время.
     */
    private String[] converterDateToString(Date date) {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String[] masStringDate = new String[2];
        masStringDate[0] = dateFormat.format(date);
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        masStringDate[1] = format2.format(date);
        return masStringDate;
    }

    /**
     * Метод вызывающий закрытие окна.
     */
    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }

    /**
     * Метод изменения выбранного объекта Flight
     *
     * @param actionEvent реакция на нажитие
     * @throws ParseException ошибка соединения
     */
    public void edit(ActionEvent actionEvent) throws ParseException {
        if (inputCheck()) {
            flight.setIdAirbus(comboAirbus.getValue());
            String dateString = datePicker.getValue().format(formatter) + " " + time.getText();
            flight.setDeparture(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateString));
            flight.setRoute(new Route(from.getValue(), to.getValue()));
            flight.setTravelTime(Integer.parseInt(travelTimeMinutes.getText()));
            stage.close();
        }
    }

    /**
     * Функция проверяющая правильность ввда значений на сцене
     *
     * @return true-без ошибокб, false-есть ошибки при вводе
     */
    private boolean inputCheck() {
        String errorMessage = "";
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
}
