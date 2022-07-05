package controller;

import helper.DialogSender;
import helper.database.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Label loginLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public TextField usernameField;
    public PasswordField passwordField;
    public Button submitBtn;
    public Label locationLabel;
    public Label zoneIdLabel;

    private ResourceBundle rb = ResourceBundle.getBundle("util/lang");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zoneId = ZoneId.systemDefault();
        zoneIdLabel.setText(zoneId.toString());
        loginLabel.setText(resourceBundle.getString("mainMessage"));
        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        usernameField.setPromptText(resourceBundle.getString("usernamePrompt"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        passwordField.setPromptText(resourceBundle.getString("passwordPrompt"));
        submitBtn.setText(resourceBundle.getString("submit"));
        locationLabel.setText(resourceBundle.getString("locationLabel"));
    }

    public void onSubmit(ActionEvent actionEvent) {
        System.out.println("Submitting login info...");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            log(false, username, password);
            DialogSender.error(rb.getString("title"), rb.getString("emptyFields"));
            System.out.println("One or both fields are empty, cancelling login...");
            return;
        }
        boolean valid = UserDB.getUser(username) != null;
        if (!valid) {
            log(false, username, password);
            DialogSender.error(rb.getString("title"), rb.getString("usernameNotFound"));
            System.out.println("Username not found, cancelling login...");
            return;
        }
        boolean matches = UserDB.passwordMatches(username, password);
        log(matches, username, password);
        if (!matches) {
            DialogSender.error(rb.getString("title"), rb.getString("passwordIncorrect"));
            System.out.println("Username found, but password incorrect. Cancelling login...");
            return;
        }
        UserDB.setCurrentUser(UserDB.getUser(username));
        System.out.println("Username and password match! Logging in...");
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/View.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(boolean success, String username, String password) {
        try {
            FileWriter fw = new FileWriter("login_activity.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a");
            String time = LocalDateTime.now().format(dtf);
            pw.println(time + (success ? " [SUCCESS] " : " [FAILURE] ") + "username: \"" + username + "\", password: \"" + password + "\"");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}