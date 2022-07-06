package main;

import helper.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * The main class of the application.
 * <p>
 *     Important lambda expression locations:
 *     <ul>
 *         <li><code>ViewController.initialize()</code></li>
 *         <li><code>LoginController.showUpcoming()</code></li>
 *     </ul>
 * <p>
 *     There are 10+ lambda expressions in my code and the requirements
 *     specify at least <b>two</b>, so I have listed two that I find most
 *     important, each with full explanations.
 *     <br/>
 *     However, I will also be pointing out and briefly explaining the rest of
 *     the lambda expressions, as listed below.
 * <p>
 *     The remaining lambda expression locations:
 *     <ul>
 *         <li>1 in <code>AppointmentController.initialize()</code></li>
 *         <li>7 in <code>ViewController.initialize()</code></li>
 *         <li>2 in <code>CustomerController.initialize()</code></li>
 *     </ul>
 *
 * @author Alex Bright
 * */
public class Main extends Application {

    private static Stage stage;

    /**
     * The entry point of the Java program.
     * Handles opening and closing of the database, and launching the JavaFX program.
     *
     * @param args args
     */
    public static void main(String[] args) {
        DatabaseConnection.openConnection();
        if (DatabaseConnection.getConnection() == null) {
            System.out.println("Connection failed! Not launching program...");
            System.exit(-1);
        }
        launch(args);
        DatabaseConnection.closeConnection();
    }

    /**
     * Launches the Scheduling System application.
     * Defines resource bundle for English/French translation.
     *
     * @param stage stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        loader.setResources(ResourceBundle.getBundle("util/lang"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(loader.getResources().getString("title"));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gets the main stage of the program.
     * Allows for easy scene switching anywhere in the program.
     *
     * @return the main stage
     */
    public static Stage getStage() {
        return Main.stage;
    }

    /**
     * Sets the main stage of the program.
     *
     * @param stage the main stage
     */
    public static void setStage(Stage stage) {
        Main.stage = stage;
    }

}
