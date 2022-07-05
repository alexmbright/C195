package controller;

import helper.DialogSender;
import helper.database.AppointmentDB;
import helper.database.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    public Tab appointmentTab;
    public RadioButton aViewAllRadio;
    public ToggleGroup aViewByToggle;
    public RadioButton aViewMonthRadio;
    public RadioButton aViewWeekRadio;
    public Button aScheduleBtn;
    public Button aModifyBtn;
    public Button aDeleteBtn;

    public Tab customerTab;
    public Button cAddBtn;
    public Button cDeleteBtn;
    public Button cModifyBtn;

    public Tab reportTab;

    public TableView<Appointment> appointmentTable;
    public TableColumn<Appointment, Integer> aIdCol;
    public TableColumn<Appointment, String> aTitleCol;
    public TableColumn<Appointment, String> aDescriptionCol;
    public TableColumn<Appointment, String> aLocationCol;
    public TableColumn<Appointment, String> aContactCol;
    public TableColumn<Appointment, String> aTypeCol;
    public TableColumn<Appointment, String> aStartCol;
    public TableColumn<Appointment, String> aEndCol;
    public TableColumn<Appointment, Integer> aCustomerCol;
    public TableColumn<Appointment, Integer> aUserCol;

    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer> cIdCol;
    public TableColumn<Customer, String> cNameCol;
    public TableColumn<Customer, String> cAddressCol;
    public TableColumn<Customer, String> cPostalCol;
    public TableColumn<Customer, String> cPhoneCol;
    public TableColumn<Customer, String> cDivisionCol;
    public TableColumn<Customer, String> cCountryCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAppointmentTable();
        initCustomerTable();
    }

    public void initAppointmentTable() {
        aIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        aTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aStartCol.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));
        aEndCol.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));
        aCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        aUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        appointmentTable.setItems(AppointmentDB.getAll());
        appointmentTable.getSortOrder().add(aIdCol);
    }

    public void initCustomerTable() {
        cIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        cPostalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        cPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        cCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        customerTable.setItems(CustomerDB.getAll());
        customerTable.getSortOrder().add(cIdCol);
    }

    public void onAddAppointment(ActionEvent actionEvent) {
        
    }

    public void onModifyAppointment(ActionEvent actionEvent) {

    }

    public void onDeleteAppointment(ActionEvent actionEvent) {

    }

    public void onAddCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddCustomer.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().isEmpty()) {
            DialogSender.warn("Delete Customer", "Please select the customer you want to delete.");
            return;
        }
        Customer c = customerTable.getSelectionModel().getSelectedItem();
        if (DialogSender.confirm("Delete Customer", "Are you sure you want to delete '" + c.getName() + "' at ID " + c.getId() + "?")) {
            if (CustomerDB.delete(c.getId()) == -1) {
                DialogSender.error("Delete Customer", "Could not delete customer from database!");
                return;
            }
            customerTable.setItems(CustomerDB.getAll());
            customerTable.getSortOrder().add(cIdCol);
            DialogSender.inform("Delete Customer", "Customer successfully deleted at ID " + c.getId());
        }
        customerTable.getSelectionModel().clearSelection();
    }

    public void onModifyCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyCustomer.fxml")));
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onViewAll(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentDB.getAll());
        appointmentTable.getSortOrder().add(aIdCol);
    }

    public void onViewMonth(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentDB.getThisMonth());
        appointmentTable.getSortOrder().add(aIdCol);
    }

    public void onViewWeek(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentDB.getThisWeek());
        appointmentTable.getSortOrder().add(aIdCol);

    }
}
