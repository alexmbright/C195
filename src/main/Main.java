package main;

import helper.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {
        // remove comment to test French translation
        // Locale.setDefault(new Locale("fr"));
        DatabaseConnection.openConnection();
        launch(args);
        DatabaseConnection.closeConnection();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        loader.setResources(ResourceBundle.getBundle("util/lang"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(loader.getResources().getString("title"));
        stage.setResizable(false);
        stage.show();
    }

}
