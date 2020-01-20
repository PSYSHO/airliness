package interfaceController;

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

/**
 * Класс-контролер отвечающий за главное окно интерфейса.
 *
 * @author Kashkinov Sergeu
 */
public class MainSceneController {

    /** Объект класса клиент взаимодействующий  сервером. */
    private Client client;

    /** Объект каркаса для работы с каркасом главной формы. */
    private Stage stage;

    /** Список рейсов. */
    private List<Flight> listFlights;

    /** Переменная показывающая, что пользователь зашел в поиск. */
    private boolean flagSearch = false;

    @FXML
    private Button search;

    @FXML
    private Label labelM;

    @FXML
    private Label labelS;

    @FXML
    private TextField searchField;

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
        try {
            client = new Client(this);
        } catch (IOException ex) {
            this.listFlights(null, "Ошибка соединения с сервером");
        }
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
        System.out.println("Список рейсов обновлен...");
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
        return new SimpleStringProperty(param.getValue().getRoute().getPointOfDeparture());
    }

    /**
     * Вспомогающий метод для отображения в таблице значения конца пути, куда полетит самолет
     *
     * @param param определяющий куда будут заполняться значение ищ return
     * @return возвращает тип StringProperty, преобразованный из типа String
     */
    private StringProperty getRouteTo(TableColumn.CellDataFeatures<Flight, String> param) {
        return new SimpleStringProperty(param.getValue().getRoute().getPointOfArrival());
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
        return formatDate;
    }

    /**
     * Метод для сортировки списка рейсов и преобразования списка в тип ObservableArrayList
     *
     * @param list список рейсов переданного с сервера
     */
    public  void listFlights(List<Flight> list, String message) {
        if (list != null) {
            listFlights = list;
            Collections.sort(listFlights);
            if (!flagSearch)
                initTableData(FXCollections.observableArrayList(listFlights));
            else {
                flagSearch = false;
                searchList();
            }
        }
        labelS.setText(message);
    }

    /**
     * Метод обновляющий значения рейсов в списке
     *
     * @param obj     - объект который передается для изменения, удаления или добавления в список
     * @param message - сообщение определяющие действия над списком
     * @param id      - айди элемента который будет орабатываться
     */
    public void listUpdate(Object obj, TypeMessage message, int id) {
        if (message != null) {
            Flight flight = (Flight) obj;
            switch (message) {
                case addFlight:
                    if (!searchId(id)) {
                        listFlights.add(flight);
                    }
                    break;
                case editFlight:
                    if (searchId(id)) {
                        int index = -1;
                        for (int j = 0; j < listFlights.size(); j++) {
                            if (listFlights.get(j).getId() == id) {
                                index = j;
                                break;
                            }
                        }
                        listFlights.set(index, flight);
                    }
                    break;
                case deleteFlight:
                    if (searchId(id)) {
                        int indexDelete = -1;
                        for (int j = 0; j < listFlights.size(); j++) {
                            if (listFlights.get(j).getId() == id) {
                                indexDelete = j;
                                break;
                            }
                        }
                        listFlights.remove(indexDelete);
                    }
                    break;
                case cannotChage:
                default:
                    break;
            }
            listFlights(listFlights, "Рейс с ID: " + id + message.getDescription());
        }
    }

    private boolean searchId(int id) {
        for(Flight flight:listFlights)
            if (id == flight.getId())
                return true;
        return false;
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
     * Присутсвуют кнопки добавления рейса и отмены.
     */
    @FXML
    public void add(ActionEvent actionEvent) {
        Stage newStage = new Stage();
        FXMLLoader addLoader = new FXMLLoader();
        newStage.setTitle(TypeMessage.addFlight.name());
        URL xmlUrl = getClass().getResource("/addSample.fxml");
        addLoader.setLocation(xmlUrl);
        try {
            Parent newRoot = addLoader.load();
            Scene newScene = new Scene(newRoot);
            newStage.setScene(newScene);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Connection");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        newStage.initModality(Modality.APPLICATION_MODAL);
        AddSampleController controller = addLoader.getController();
        controller.setStage(newStage, listFlights);
        newStage.showAndWait();
        if (controller.isOnClick()) {
            try {
                client.receivingMessage(TypeMessage.addFlight, controller.getFlight(), controller.getFlight().getId());
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
            Flight obj = tableFlights.getItems().get(index);
            // tableFlights.getItems().remove(index);
            client.receivingMessage(TypeMessage.deleteFlight, obj, obj.getId());
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
        editStage.setTitle(TypeMessage.editFlight.name());
        URL editURL = getClass().getResource("/editSample.fxml");
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
            client.receivingMessage(TypeMessage.editFlight, flightEdit, flightEdit.getId());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    /**
     * Метод запускающийся при старте приложения и отправляющий команду серверу о возврате списка рейсов.
     * @param stage - каркас.
     */
    public void start(Stage stage) {
        this.stage = stage;
        int index = -1;
        try {
            client.receivingMessage(TypeMessage.getFlight, null, index);
            flagSearch = false;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No connection");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Please check your connection to the server");
            alert.showAndWait();
            tableFlights.getAccessibleHelp();
        }
    }

    /**
     * Метод запрашивающий полный список у сервера.
     */
    @FXML
    public void getFlights(ActionEvent actionEvent) {
        start(stage);
    }

    /**
     * Метод поиска подстроки в строке.
     */
    @FXML
    public void search(ActionEvent actionEvent) {
        searchList();
    }

    private void searchList(){
        List<Flight> listSearch = new ArrayList<Flight>();
        if (isClick()) {
            String searchString = searchField.getText();
            for (Flight flight : listFlights) {
                int i = 0; //текущее положение курсора
                boolean flag = false; //зашел в цикл поиска
                int lengthSearch = searchString.length();
                int lengthFlight = flight.toString().length();
                exit:
                while (i + lengthSearch < lengthFlight - 1) {
                    int j = 0;
                    flag = false;
                    while ((searchString.charAt(j) == flight.toString().charAt(i)) && (j <= lengthSearch - 1)) {
                        if (j == lengthSearch - 1) {
                            listSearch.add(flight);
                            System.out.println(flight);
                            break exit;
                        }
                        flag = true;
                        i++;
                        j++;
                    }
                    if (!flag) {
                        i++;
                    }
                }
            }
            if (listSearch.size() != 0) {
                listFlights(listSearch, "Поиск по запросу " + searchString + " прошел успешно...");
            } else
                listFlights(listSearch, "Поиск по запросу " + searchString + " не нашел ни одного совпадения...");
            flagSearch = true;
        }
    }

    /**
     * Метод проверяющий было ли введено значение в поле поиска.
     * @return если было что то введено - treu, нет - false.
     */
    private boolean isClick() {
        String messageError = "";
        if (searchField.getText().length() == 0) {
            messageError += "Nothing entered ib the field";
        }
        if (messageError.length() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(messageError);
            alert.showAndWait();

            return false;
        } else {
            return true;
        }
    }
}

