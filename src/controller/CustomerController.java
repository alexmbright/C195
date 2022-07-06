package controller;

import helper.DialogSender;
import helper.database.CountryDB;
import helper.database.CustomerDB;
import helper.database.DivisionDB;
import javafx.event.ActionEvent;
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

public class CustomerController implements Initializable {

    public Label headerLabel;
    public Label descLabel;

    public TextField idField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalField;
    public TextField phoneField;

    public Label divisionLabel;
    public ComboBox<Division> divisionCombo;
    public ComboBox<Country> countryCombo;

    public Button submitBtn;
    public Button cancelBtn;
    public Label errorLabel;

    private boolean update;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(CountryDB.getAll());
        countryCombo.setPromptText("Select country");
        submitBtn.setLayoutY(divisionCombo.getLayoutY());
        cancelBtn.setLayoutY(divisionCombo.getLayoutY());
        errorLabel.setLayoutY(divisionCombo.getLayoutY() + 4);
        divisionLabel.setVisible(false);
        divisionCombo.setVisible(false);

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

    public void populate(Customer c) {
        if (c == null) {
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

        idField.setPromptText(Integer.toString(c.getId()));
        nameField.setText(c.getName());
        addressField.setText(c.getAddress());
        postalField.setText(c.getPostal());
        phoneField.setText(c.getPhone());

        countryCombo.setItems(CountryDB.getAll());
        countryCombo.getSelectionModel().select(c.getCountry());
        submitBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
        cancelBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
        errorLabel.setLayoutY(divisionCombo.getLayoutY() + 54);
        divisionCombo.setVisible(true);
        divisionLabel.setVisible(true);
        divisionCombo.setItems(DivisionDB.getAllByCountry(countryCombo.getSelectionModel().getSelectedItem().getId()));
        divisionCombo.getSelectionModel().select(c.getDivision());

        update = true;
    }

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
