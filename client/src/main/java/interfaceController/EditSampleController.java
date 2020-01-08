package sample.interfaceController;

import general.Airbus;
import general.Flight;
import general.Route;
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
import java.util.Date;
import java.util.List;

public class EditSampleController {
    private Stage stage;
    private Flight flight;
    private DateTimeFormatter formatter;
    private SimpleDateFormat dateFormat;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField id;

    @FXML
    private ComboBox<Airbus> comboAirbus;

    @FXML
    private TextField time;

    @FXML
    private TextField travelTimeMinutes;

    @FXML
    private TextField from;

    @FXML
    private TextField to;

    /**
     * Инициализирует ComboBox элементами типа Airbus,
     * инициализирует formatter для нужного мне перевода из
     * LocalDate в тип String, инициализирует dateFormat для
     * нужного мне вывода строки на экране.
     */
    @FXML
    private void initialize(){
        comboAirbus.setItems(FXCollections.observableArrayList(Airbus.values()));
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

    public void setEditStage(Stage stage){
        this.stage=stage;
    }

    /**
     * Выводит первичные значения объекта, выбранного для редактирования.
     * @param flight объект выбранный для редактирования
     */
    public void setEditFlight(Flight flight){
        this.flight=flight;
        id.setText(Integer.toString(flight.getId()));
        comboAirbus.setValue(flight.getIdAirbus());
        String[] dateString=converterDateToString(flight.getDeparture())[0].split("\\.");
        int day=Integer.parseInt(dateString[0]);
        int mouth=Integer.parseInt(dateString[1]);
        int year=Integer.parseInt(dateString[2]);
        datePicker.setValue(LocalDate.of(year,mouth,day));
        time.setText(converterDateToString(flight.getDeparture())[1]);
        from.setText(flight.getRoute().getPointOfDeparture());
        to.setText(flight.getRoute().getPointOfArrival());
        travelTimeMinutes.setText(Integer.toString(flight.getTravelTime()));
    }

    private String[] converterDateToString (Date date){
        dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        String[] masStringDate=new String[2];
        masStringDate[0]=dateFormat.format(date);
        SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
        masStringDate[1]=format2.format(date);
        return masStringDate;
    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }


    public void edit(ActionEvent actionEvent) throws ParseException {
        if(inputCheck()) {
            flight.setId(Integer.parseInt(id.getText()));
            flight.setIdAirbus(comboAirbus.getValue());
            String dateString = datePicker.getValue().format(formatter) + " " + time.getText();
            flight.setDeparture(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateString));
            String fromString = from.getText();
            String toString = to.getText();
            flight.setRoute(new Route(fromString, toString));
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
        if ((id.getText() == null) || (id.getText().length() == 0)) {
            errorMessage += "Empty field Id";
        } else {
            try {
                Integer.parseInt(id.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Wrong Id field number input format";
            }
        }
        if (comboAirbus.getValue() == null) {
            errorMessage += "Empty field Airbus";
        }
        if ((datePicker.getValue() == null) || ((time.getText() == null) || (time.getText().length() == 0))) {
            errorMessage += "Empty field Date or Time";
        } else {
            try {
                String dateString = datePicker.getValue().format(formatter) + " " + time.getText();
                Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateString);
            } catch (ParseException e) {
                errorMessage += "Wrong date and time format";
            }
        }
        if ((from.getText() == null) || (from.getText().length() == 0)) {
            errorMessage += "Empty field From";
        }
        if ((to.getText() == null) || (to.getText().length() == 0)) {
            errorMessage += "Empty field To";
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
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }

    }
}
