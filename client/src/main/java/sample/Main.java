package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import interfaceController.MainSceneController;

import java.io.IOException;
import java.net.URL;

/**
 * Класс выплняющий запуск приложения.
 *
 * @author Kashkinov Sergeu
 */
public class Main extends Application {
    private MainSceneController controller;

    /**
     * Метод задающий основные свойства гланого окна приложения.
     *
     * @param primaryStage - каркас на котором стоит главное окно интерфейса.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            URL xmlUrl = getClass().getResource("/sample.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            primaryStage.setTitle("Авиарейсы");
            primaryStage.setScene(new Scene(root, 800, 585));
            primaryStage.show();
            controller = loader.getController();
            controller.start(primaryStage);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    if (1 == controller.showConfirmation())
                        we.consume();
                }
            });
        } catch (LoadException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Connection");
            alert.setHeaderText("Нет соединения с сервером");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод запускающий работу интерфейса.
     *
     * @param args - параметры командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
