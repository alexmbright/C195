package controller;

import helper.DialogSender;
import helper.database.AppointmentDB;
import helper.database.UserDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller for the Login screen.
 *
 * @author Alex Bright
 */
public class LoginController implements Initializable {

    public Label loginLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public TextField usernameField;
    public PasswordField passwordField;
    public Button submitBtn;
    public Label locationLabel;
    public Label zoneIdLabel;

    private ResourceBundle rb;

    /**
     * Initializes the LoginController.
     * This method determines the user's location and displays in the form.
     * It also translates all login screen messages using provided resource bundle.
     *
     * @param url
     * @param resourceBundle resource bundle used for language translation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        ZoneId zoneId = ZoneId.systemDefault();
        zoneIdLabel.setText(zoneId.toString());
        loginLabel.setText(resourceBundle.getString("mainMessage"));
        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        submitBtn.setText(resourceBundle.getString("submit"));
        locationLabel.setText(resourceBundle.getString("locationLabel"));
    }

    /**
     * Validates and logs input on submit button click.
     * On successful login, switches to View screen and calls <code>showUpcoming()</code> method to display upcoming appointments.
     *
     * @param actionEvent
     */
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
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.setTitle("Scheduling System");
            stage.show();
            showUpcoming();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the appropriate upcoming appointment dialog box.
     * If no upcoming appointments, displays a message stating that.
     * Otherwise, display count and list of all upcoming appointments.
     * <p>
     *     <b>Lambda expression:</b> I used the forEach lambda expression to append all upcoming appointment
     *     information to a StringBuilder for display. This allows for more efficiency as I don't need to
     *     loop through and create separate String instances for each iteration.
     * </p>
     */
    public void showUpcoming() {
        ObservableList<Appointment> upcoming = AppointmentDB.getUpcoming();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        if (upcoming.size() == 0) {
            DialogSender.inform("Appointments", "There are no upcoming appointments.");
            return;
        }
        StringBuilder alert = new StringBuilder();
        alert.append(upcoming.size() > 1 ? "There are " + upcoming.size() + " upcoming appointments:\n" : "There is 1 upcoming appointment:\n");

        //lambda expression
        upcoming.forEach(appt -> alert.append("\n\t").append(appt.getId()).append(" - '").append(appt.getTitle()).append("' at ").append(appt.getStart().format(dtf)));

        DialogSender.warn("Appointments", alert.toString());
    }

    /**
     * Writes login attempt details with timestamp to 'login_activity.txt' log file.
     *
     * @param success true if login was successful, otherwise false
     * @param username username of login attempt
     * @param password password of login attempt
     */
    private void log(boolean success, String username, String password) {
        try {
            FileWriter fw = new FileWriter("login_activity.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
            String time = LocalDateTime.now().format(dtf);
            pw.println(time + (success ? " [SUCCESS] " : " [FAILURE] ") + "username: \"" + username + "\", password: \"" + password + "\"");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}