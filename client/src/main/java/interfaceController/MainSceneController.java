package sample.interfaceController;

import general.Airbus;
import general.Flight;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Client;
import general.TypeMessage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainSceneController {
    private Client client;

    @FXML
    private TableView<Flight> tableFlights;

    @FXML
    private TableColumn<Flight, Integer> id;

    @FXML
    private TableColumn<Flight, Airbus> airbus;

    @FXML
    private TableColumn<Flight, String> from;

    @FXML
    private TableColumn<Flight, String> to;

    @FXML
    private TableColumn<Flight, String> date;

    @FXML
    private TableColumn<Flight, String> time;

    @FXML
    private TableColumn<Flight, String> travelTimeMinutes;

    /**
     * Конструктор инициализирующий клиента и передающий ему параметр типа MainSceneController
     */
    public MainSceneController() {
    }


    /**
     * Метод вызывающийся автоматически, инициализирует TableView
     * какие типы типы данных будут заполнять таблицу
     */
    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        airbus.setCellValueFactory(new PropertyValueFactory<>("idAirbus"));
        travelTimeMinutes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flight, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Flight, String> param) {
                return getTravelTimeMinutes(param);
            }
        });
        from.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flight, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Flight, String> param) {
                return getRouteFrom(param);
            }
        });
        to.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flight, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Flight, String> param) {
                return getRouteTo(param);
            }
        });
        date.setCellValueFactory(param -> getDateFromDate(param));
        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flight, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Flight, String> param) {
                return getTimeFromDate(param);
            }
        });
    }


    /**
     * Вспомогающий метод перевоящий значение типа int  в значение понятное таблице
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String, он в свою очередь преобразован из типа int
     */
    private StringProperty getTravelTimeMinutes(TableColumn.CellDataFeatures<Flight, String> param) {
        return new SimpleStringProperty(String.valueOf(param.getValue().getTravelTime()));
    }

    /**
     * Вспомогающий метод для отображения значения начала, откуда полетит самолет,в таблице
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String
     */
    private StringProperty getRouteFrom(TableColumn.CellDataFeatures<Flight, String> param) {
        return new SimpleStringProperty(param.getValue().getRoute().getPointOfArrival());
    }

    /**
     * Вспомогающий метод для отображения в таблице значения конца пути, куда полетит самолет
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String
     */
    private StringProperty getRouteTo(TableColumn.CellDataFeatures<Flight, String> param) {
        System.out.println("getRouteTo");
        return new SimpleStringProperty(param.getValue().getRoute().getPointOfDeparture());
    }

    /**
     * Вспомогающий метод для отображения даты рейса
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String, он в свою очередь преобразован из типа Date
     */
    private StringProperty getDateFromDate(TableColumn.CellDataFeatures<Flight, String> param) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        StringProperty formatDate = new SimpleStringProperty(format.format(param.getValue().getDeparture()));
        System.out.println(format.format(param.getValue().getDeparture()));
        return formatDate;
    }

    /**
     * Вспомогающий метод для отображения времени рейса
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String, он в свою очередь преобразован из типа Date
     */
    private StringProperty getTimeFromDate(TableColumn.CellDataFeatures<Flight, String> param) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        StringProperty formatDate = new SimpleStringProperty(format.format(param.getValue().getDeparture()));
        System.out.println(format.format(param.getValue().getDeparture()));
        return formatDate;
    }

    /**
     * Метод для сортировки списка рейсов и преобразования списка в тип ObservableArrayList
     *
     * @param list список рейсов переданного с сервера
     */
    public void listFlights(List<Flight> list) {
        Collections.sort(list);
        initTableData(FXCollections.observableArrayList(list));
    }

    /**
     * Метод инициализирующий таблицу tableFlights значениями
     *
     * @param list передается параметр типа ObservableList, для инициализации таблицы
     */
    public void initTableData(ObservableList<Flight> list) {
        tableFlights.setItems(list);
    }

    /**
     * Метод реагирующий на нажатие кнопки клиентом.
     * Создается новая сцена на кототорой пользователю предложенно
     * заполнить форму добавления нового рейса.
     * Присутсвуют кнопки добавления рейса и отмены
     *
     * @throws IOException ошибка соединения
     */
    @FXML
    public void add(ActionEvent actionEvent){
        Stage newStage = new Stage();
        FXMLLoader addLoader = new FXMLLoader();
        newStage.setTitle(TypeMessage.addFlight.getMessage());
        URL xmlUrl = getClass().getResource("/sample/fxmlFile/addSample.fxml");
        addLoader.setLocation(xmlUrl);
        try {
            Parent newRoot = addLoader.load();
            Scene newScene = new Scene(newRoot);
            newStage.setScene(newScene);
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Connection");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        newStage.initModality(Modality.APPLICATION_MODAL);
        AddSampleController controller = addLoader.getController();
        controller.setStage(newStage);
        newStage.showAndWait();
        if(controller.isOnClick()) {
            try {
                client.add(TypeMessage.addFlight.getMessage(), controller.getFlight(), -1);
            } catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No connection");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Please check your connection to the server");
                alert.showAndWait();
                tableFlights.getAccessibleHelp();
            }
        }
    }


    /**
     * Метод реагирующий на нажатие кнопки. Реализует
     * выход из приложения и передают сигнал клиенту о
     * закрытие сокета и всех потоков.
     *
     * @param actionEvent реакция на нажатие
     * @throws IOException ошибка соединения
     */
    @FXML
    public void exit(ActionEvent actionEvent) throws IOException {
        if (1 == showConfirmation())
            actionEvent.consume();
    }

    /**
     * Подтверждение выхода из приложения
     */
    public int showConfirmation() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Window exit");
        exitAlert.setHeaderText("Вы действительно хотите выйти?");
        Optional<ButtonType> optional = exitAlert.showAndWait();
        if (optional.get() == ButtonType.OK) {
            try {
                client.stop();
                System.exit(0);
            } catch (IOException e) {
            }
        }
        if (optional.get() == ButtonType.CANCEL) {
            try {
                return 1;
            } catch (Exception e) {
            }
        }
        return 0;
    }

    /**
     * Метод реагирующий на нажатие кнопки.
     * Выполняет удаление выбранного элемента и передают запрос
     * клиенту, что бы он уведомил сервер о произошедших изменениях.
     * При не выборе не одного из элементов таблицы, появится окно
     * уведомляющее пользователя о необходимости выбора одного из элементов.
     *
     * @param actionEvent реакция на нажатие
     */
    @FXML
    public void delete(ActionEvent actionEvent) throws IOException {
        int index = tableFlights.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            tableFlights.getItems().remove(index);
            Flight obj=new Flight();
            String message=TypeMessage.deleteFlight.getMessage();
            client.deleteFlight(message,obj,index);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    /**
     * Метод реагирующий на нажатие кнопки клиентом.
     * Создается новая сцена на кототорой пользователю предложенно
     * изменить данные выбранного рейса.
     * При нажатии кнопки и не выбора одного из полей таблицы,
     * появится окно уведомляющее,что нужно сделать выбор.
     * Присутсвуют кнопки добавления рейса и отмены
     *
     * @throws IOException ошибка соединения
     */
    @FXML
    public void edit(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        FXMLLoader editLoader = new FXMLLoader();
        editStage.setTitle(TypeMessage.editFlight.getMessage());
        URL editURL = getClass().getResource("/sample/fxmlFile/editSample.fxml");
        editLoader.setLocation(editURL);
        Parent root = editLoader.load();
        Scene editScene = new Scene(root);
        editStage.setScene(editScene);
        editStage.initModality(Modality.APPLICATION_MODAL);
        EditSampleController controller = editLoader.getController();
        if (tableFlights.getSelectionModel().getSelectedIndex() > -1) {
            controller.setEditStage(editStage);
            Flight flightEdit = tableFlights.getSelectionModel().getSelectedItem();
            controller.setEditFlight(flightEdit);
            editStage.showAndWait();
            String message=TypeMessage.editFlight.getMessage();
            client.edit(flightEdit,message, tableFlights.getSelectionModel().getSelectedIndex());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    public void start() {
        String message = TypeMessage.getFlight.getMessage();
        Flight obj = new Flight();
        int index = -1;
        try {
            client = new Client(this);
            client.getAllFlight(message, obj, index);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No connection");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Please check your connection to the server");
            alert.showAndWait();
            tableFlights.getAccessibleHelp();
        }
    }
}

