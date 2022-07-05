package controller;

import helper.database.DivisionDB;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Country;
import model.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

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


        // same lambda as located in AddCustomerController (ignore)
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

    }

}
