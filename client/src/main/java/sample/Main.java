package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import interfaceController.MainSceneController;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader=new FXMLLoader();
            URL xmlUrl=getClass().getResource("fxmlFile/sample.fxml");
            loader.setLocation(xmlUrl);
            Parent root=loader.load();
            primaryStage.setTitle("Авиарейсы");
            primaryStage.setScene(new Scene(root, 800, 550));
            primaryStage.show();
            MainSceneController controller=loader.getController();
            controller.start();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    if(1==showConfirmation())
                        we.consume();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Подтверждение выхода из приложения
     */
    public int showConfirmation()  {
        Alert exitAlert=new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Window exit");
        exitAlert.setHeaderText("Вы действительно хотите выйти?");
        Optional<ButtonType> optional=exitAlert.showAndWait();
        if( optional.get()==ButtonType.CANCEL){
            try {
                return 1;
            } catch (Exception e){
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
