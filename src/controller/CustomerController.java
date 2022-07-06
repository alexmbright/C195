package controller;

import helper.DialogSender;
import helper.database.CountryDB;
import helper.database.CustomerDB;
import helper.database.DivisionDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the customer register/update forms.
 *
 * @author Alex Bright
 */
public class CustomerController implements Initializable {

    @FXML
    private Label headerLabel;
    @FXML
    private Label descLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalField;
    @FXML
    private TextField phoneField;

    @FXML
    private Label divisionLabel;
    @FXML
    private ComboBox<Division> divisionCombo;
    @FXML
    private ComboBox<Country> countryCombo;

    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorLabel;

    private boolean update;

    /**
     * Initializes the CustomerController.
     * This method fills the form's ComboBox fields, initializes the update boolean to false,
     * and sets functionalities for Country ComboBox selection and cancel button click.
     * <p>
     *     There are two lambda expressions found in <code>CustomerController.initialize()</code>.
     *     The first one defines the functionality of the Country ComboBox, which updates
     *     the Division ComboBox (and displays it when Country is first selected).
     *     The second one defines the functionality of the Cancel button, which returns the
     *     user to the View screen on confirmation.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(CountryDB.getAll());
        countryCombo.setPromptText("Select country");
        submitBtn.setLayoutY(divisionCombo.getLayoutY());
        cancelBtn.setLayoutY(divisionCombo.getLayoutY());
        errorLabel.setLayoutY(divisionCombo.getLayoutY() + 4);
        divisionLabel.setVisible(false);
        divisionCombo.setVisible(false);

        // lambda expression
        countryCombo.setOnAction(event -> {
            divisionLabel.setVisible(true);
            divisionCombo.setVisible(true);
            divisionCombo.setItems(DivisionDB.getAllByCountry(countryCombo.getSelectionModel().getSelectedItem().getId()));
            divisionCombo.getSelectionModel().select(null);
            divisionCombo.setPromptText("Select division");
            submitBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
            cancelBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
            errorLabel.setLayoutY(divisionCombo.getLayoutY() + 54);
        });

        update = false;

        // lambda expression
        cancelBtn.setOnAction(event -> {
            if (DialogSender.confirm((update ? "Update" : "Add") + " Customer", "Are you sure you want to cancel " + (update ? "updating" : "adding") + " this customer?")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
                    Scene scene = new Scene(loader.load());
                    ((ViewController)loader.getController()).setTab(1);
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
     * Populates the form with pre-selected customer information.
     * This method also sets the update boolean to true, which indicates this form
     * is being used to update an appointment.
     *
     * @param customer selected customer
     */
    public void populate(Customer customer) {
        if (customer == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
                Scene scene = new Scene(loader.load());
                ((ViewController)loader.getController()).setTab(1);
                Stage stage = Main.getStage();
                stage.setScene(scene);
                stage.show();
                DialogSender.error("Update Customer", "Selected customer variable not initialized! Please try again.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        headerLabel.setText("Customer Update");
        descLabel.setText("Please enter the updated customer information.");

        idField.setPromptText(Integer.toString(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalField.setText(customer.getPostal());
        phoneField.setText(customer.getPhone());

        countryCombo.setItems(CountryDB.getAll());
        countryCombo.getSelectionModel().select(customer.getCountry());
        submitBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
        cancelBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
        errorLabel.setLayoutY(divisionCombo.getLayoutY() + 54);
        divisionCombo.setVisible(true);
        divisionLabel.setVisible(true);
        divisionCombo.setItems(DivisionDB.getAllByCountry(countryCombo.getSelectionModel().getSelectedItem().getId()));
        divisionCombo.getSelectionModel().select(customer.getDivision());

        update = true;
    }

    /**
     * Validates input and inserts/updates customer.
     * This method checks for blank input fields and successful insert/update in the database.
     * If successful, returns to the main screen with a dialog box.
     *
     * @param actionEvent action event
     */
    public void onSubmit(ActionEvent actionEvent) {
        String error = "";
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || countryCombo.getSelectionModel().getSelectedItem() == null
                || divisionCombo.getSelectionModel().getSelectedItem() == null)
            error += "Please fill out all fields!";
        errorLabel.setText(error);
        if (!errorLabel.getText().isBlank()) return;

        int id;
        if (update) {
            id = Integer.parseInt(idField.getPromptText());
            if (CustomerDB.update(id, nameField.getText(), addressField.getText(), postalField.getText(), phoneField.getText(), divisionCombo.getSelectionModel().getSelectedItem().getId()) == -1) {
                DialogSender.error("Update Customer", "Could not update customer in database!");
                return;
            }
        } else {
            id = CustomerDB.add(nameField.getText(), addressField.getText(), postalField.getText(), phoneField.getText(), divisionCombo.getSelectionModel().getSelectedItem().getId());
            if (id == -1) {
                DialogSender.error("Add Customer", "Could not add customer to database!");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
            Scene scene = new Scene(loader.load());
            ((ViewController)loader.getController()).setTab(1);
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
            DialogSender.inform((update ? "Update" : "Add") + " Customer", "Customer successfully " + (update ? "updated" : "added") + " at ID " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
