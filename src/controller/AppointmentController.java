package controller;

import helper.DialogSender;
import helper.database.AppointmentDB;
import helper.database.ContactDB;
import helper.database.CustomerDB;
import helper.database.UserDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * The controller for the appointment schedule/update forms.
 *
 * @author Alex Bright
 */
public class AppointmentController implements Initializable {

    @FXML
    private Label headerLabel;
    @FXML
    private Label headerDescLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descField;
    @FXML
    private TextField locField;
    @FXML
    private ComboBox<Contact> contactCombo;
    @FXML
    private TextField typeField;
    @FXML
    private DatePicker startPicker;
    @FXML
    private ComboBox<String> startCombo;
    @FXML
    private DatePicker endPicker;
    @FXML
    private ComboBox<String> endCombo;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private ComboBox<User> userCombo;

    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorLabel;

    private boolean update;

    /**
     * Initializes the AppointmentController.
     * This method fills the form's ComboBox fields, initializes the update boolean to false,
     * and defines the cancel button's functionality.
     * <p>
     *     The lambda expression found in <code>cancelBtn.setOnAction()</code> defines the
     *     functionality of the Cancel button located on the Appointment form.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCombo.setItems(CustomerDB.getAll());
        contactCombo.setItems(ContactDB.getAll());
        userCombo.setItems(UserDB.getAll());
        userCombo.getSelectionModel().select(UserDB.getCurrentUser());

        ObservableList<String> times = AppointmentDB.getValidTimes();

        startCombo.setItems(times);
        endCombo.setItems(times);

        update = false;

        // lambda expression
        cancelBtn.setOnAction(event -> {
            if (DialogSender.confirm((update ? "Update" : "Schedule") + " Appointment", "Are you sure you want to cancel " + (update ? "updating" : "scheduling") + " an appointment?")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
                    Scene scene = new Scene(loader.load());
                    ((ViewController)loader.getController()).setTab(0);
                    Stage stage = Main.getStage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Populates the form with pre-selected appointment information.
     * This method also sets the update boolean to true, which indicates this form
     * is being used to update an appointment.
     *
     * @param a selected appointment
     */
    public void populate(Appointment a) {
        if (a == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
                Scene scene = new Scene(loader.load());
                ((ViewController)loader.getController()).setTab(1);
                Stage stage = Main.getStage();
                stage.setScene(scene);
                stage.show();
                DialogSender.error("Update Appointment", "Selected appointment variable is not initialized! Please try again.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        headerLabel.setText("Update Appointment");
        headerDescLabel.setText("Please enter the updated appointment information.");

        LocalDateTime start = a.getStart();
        LocalDateTime end = a.getEnd();

        idField.setPromptText(Integer.toString(a.getId()));
        titleField.setText(a.getTitle());
        descField.setText(a.getDescription());
        locField.setText(a.getLocation());
        contactCombo.getSelectionModel().select(ContactDB.getContact(a.getContactId()));
        typeField.setText(a.getType());
        startPicker.setValue(start.toLocalDate());
        endPicker.setValue(end.toLocalDate());
        customerCombo.getSelectionModel().select(CustomerDB.getCustomer(a.getCustomerId()));
        userCombo.getSelectionModel().select(UserDB.getUser(a.getUserId()));

        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();

        startCombo.getSelectionModel().select(String.format("%02d", startTime.getHour()) + ":" + String.format("%02d", startTime.getMinute()));
        endCombo.getSelectionModel().select(String.format("%02d", endTime.getHour()) + ":" + String.format("%02d", endTime.getMinute()));

        update = true;
    }

    /**
     * Validates input and inserts/updates appointment.
     * This method checks for blank input fields, appointment time overlapping,
     * and successful insert/update in the database.
     * If successful, returns to the main screen with a dialog box.
     *
     * @param actionEvent action event
     */
    public void onSubmit(ActionEvent actionEvent) {
        errorLabel.setText("");
        if (titleField.getText().isBlank() || descField.getText().isBlank() || locField.getText().isBlank()
                || contactCombo.getSelectionModel().getSelectedItem() == null || typeField.getText().isBlank()
                || startPicker.getValue() == null || startCombo.getSelectionModel().getSelectedItem() == null
                || endPicker.getValue() == null || endCombo.getSelectionModel().getSelectedItem() == null
                || customerCombo.getSelectionModel().getSelectedItem() == null || userCombo.getSelectionModel().getSelectedItem() == null) {
            errorLabel.setText("Please fill out all fields!");
            return;
        }
        LocalDateTime start = LocalDateTime.of(startPicker.getValue(), LocalTime.parse(startCombo.getSelectionModel().getSelectedItem()));
        LocalDateTime end = LocalDateTime.of(endPicker.getValue(), LocalTime.parse(endCombo.getSelectionModel().getSelectedItem()));
        if (end.isBefore(start) || end.isEqual(start)) {
            errorLabel.setText("End time must be later than start time!");
            return;
        }
        if (update ? AppointmentDB.overlaps(customerCombo.getSelectionModel().getSelectedItem().getId(), start, end, Integer.parseInt(idField.getPromptText())) : AppointmentDB.overlaps(customerCombo.getSelectionModel().getSelectedItem().getId(), start, end)) {
            errorLabel.setText("Time overlaps with customer's existing appointment(s)");
            return;
        }
        String title = titleField.getText();
        String desc = descField.getText();
        String loc = locField.getText();
        String type = typeField.getText();
        int custId = customerCombo.getSelectionModel().getSelectedItem().getId();
        int userId = userCombo.getSelectionModel().getSelectedItem().getId();
        int contId = contactCombo.getSelectionModel().getSelectedItem().getId();

        int id;
        if (update) {
            id = Integer.parseInt(idField.getPromptText());
            if (AppointmentDB.update(id, title, desc, loc, type, start, end, custId, userId, contId) == -1) {
                DialogSender.error("Update Appointment", "Could not update appointment in database!");
                return;
            }
        } else {
            id = AppointmentDB.add(title, desc, loc, type, start, end, custId, userId, contId);
            if (id == -1) {
                DialogSender.error("Schedule Appointment", "Could not add appointment to database!");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
            Scene scene = new Scene(loader.load());
            ((ViewController)loader.getController()).setTab(0);
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
            DialogSender.inform("Schedule Appointment", "Appointment successfully " + (update ? "updated" : "scheduled") + " at ID " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
