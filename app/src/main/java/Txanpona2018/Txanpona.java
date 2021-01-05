package Txanpona2018;

import Txanpona2018.control.ui.MainUIKud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Txanpona extends Application {

    private Parent mainUI;
    private Stage stage;
    private Scene sceneM;

    private MainUIKud mainUIKud;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        stage.setTitle("Txanpona");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        mainUI = (Parent) loader.load();
        sceneM = new Scene(mainUI);

        stage.setScene(sceneM);
        stage.show();
    }
}
