package sample.interfaceController;

import general.Airbus;
import general.Flight;
import general.Route;
import general.TravelCities;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    public Flight getFlight() {
        return flight;
    }

    private ArrayList<String> getMasNameTown(){
        ArrayList<String> masNameTown=new ArrayList<String>();
        for(TravelCities element: TravelCities.values()){
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
        to.setItems(FXCollections.observableArrayList(getMasNameTown()));
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

    public void setStage(Stage stage,List<Flight> oldFlights) {
        this.stage = stage;
        this.oldFlights=oldFlights;
    }

    @FXML
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
            errorMessage += "Empty field Id";
        } else {

            try {
                if(searchId(Integer.parseInt(id.getText()))){
                    errorMessage += "Id value is repeated";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Wrong Id field number input format";
            }
        }

        if (comboAirbus.getValue() == null) {
            errorMessage += "Empty field Airbus";
        }
        if ( (datePicker.getValue() == null) || ((time.getText() == null) || (time.getText().length() == 0))) {
            errorMessage += "Empty field Date or Time";
        } else {
            try {
                String dateString = datePicker.getValue().format(formatter) + " " + time.getText();
                Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateString);
            } catch (ParseException e) {
                errorMessage += "Wrong date and time format";
            }
        }
        if (from.getValue() == null) {
            errorMessage += "Empty field From";
        }
        if (to.getValue() == null) {
            errorMessage += "Empty field To";
        }
        if (from.getValue().equals(to.getValue())){
            errorMessage += "Сan't be the same way";
        }
        if ((travelTimeMinutes.getText() == null) || (travelTimeMinutes.getText().length() == 0)) {
            errorMessage += "Empty field Id";
        } else {
            try {
                Integer.parseInt(travelTimeMinutes.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Wrong Id field number input format";
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }

    }

    /**
     * Поиск повторяющийхс уникальных ключей
     */
    public boolean searchId(int id){
        for(Flight flight:oldFlights){
            if (flight.getId()==id){
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
