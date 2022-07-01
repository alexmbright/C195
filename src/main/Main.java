package main;

import helper.database.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        DatabaseConnection.openConnection();
        launch(args);
        DatabaseConnection.closeConnection();
    }

    @Override
    public void start(Stage stage) {
//        Parent root = new FXMLLoader(getClass().getResource("/view/Login.fxml")).load();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.setTitle("Appointment Scheduler");
        stage.show();
    }
}
