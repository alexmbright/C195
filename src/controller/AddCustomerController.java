package controller;

import helper.DialogSender;
import helper.database.CountryDB;
import helper.database.CustomerDB;
import helper.database.DivisionDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    public TextField nameField;
    public TextField addressField;
    public TextField postalField;
    public TextField phoneField;

    public Label divisionLabel;
    public ComboBox<Division> divisionCombo;
    public ComboBox<Country> countryCombo;

    public Button submitBtn;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(CountryDB.getAll());
        countryCombo.setPromptText("Select country");
        submitBtn.setLayoutY(divisionCombo.getLayoutY());
        divisionLabel.setVisible(false);
        divisionCombo.setVisible(false);

        // lambda expression
        // update division ComboBox on country selection
        countryCombo.setOnAction(e -> {
            divisionLabel.setVisible(true);
            divisionCombo.setVisible(true);
            divisionCombo.setItems(DivisionDB.getAllByCountry(countryCombo.getSelectionModel().getSelectedItem().getId()));
            divisionCombo.getSelectionModel().clearSelection();
            divisionCombo.setPromptText("Select division");
            submitBtn.setLayoutY(divisionCombo.getLayoutY() + 50);
        });
    }

    public void onSubmit(ActionEvent actionEvent) {
        String error = "";
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || countryCombo.getSelectionModel().isEmpty()
                || divisionCombo.getSelectionModel().isEmpty())
            error += "Please fill out all fields!";
        errorLabel.setText(error);
        if (!errorLabel.getText().isBlank()) return;

        int newId = CustomerDB.add(nameField.getText(), addressField.getText(), postalField.getText(), phoneField.getText(), divisionCombo.getSelectionModel().getSelectedItem().getId());
        if (newId == -1) {
            DialogSender.error("Add Customer", "Could not add customer to database!");
            return;
        }

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/View.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            DialogSender.inform("Add Customer", "Customer successfully added at ID " + newId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
