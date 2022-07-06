package controller;

import helper.DialogSender;
import helper.database.AppointmentDB;
import helper.database.ContactDB;
import helper.database.CustomerDB;
import helper.database.UserDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    
    public TabPane tabPane;
    public Tab customerTab;
    public Tab reportTab;
    public Tab appointmentTab;
    
    // Appointment tab
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
    public RadioButton aViewAllRadio;
    public ToggleGroup aViewByToggle;
    public RadioButton aViewMonthRadio;
    public RadioButton aViewWeekRadio;
    public Button aScheduleBtn;
    public Button aUpdateBtn;
    public Button aCancelBtn;

    // Customer tab
    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer> cIdCol;
    public TableColumn<Customer, String> cNameCol;
    public TableColumn<Customer, String> cAddressCol;
    public TableColumn<Customer, String> cPostalCol;
    public TableColumn<Customer, String> cPhoneCol;
    public TableColumn<Customer, Division> cDivisionCol;
    public TableColumn<Customer, Country> cCountryCol;
    public Button cAddBtn;
    public Button cDeleteBtn;
    public Button cUpdateBtn;
    
    // Type month report
    public ComboBox<String> tmTypeCombo;
    public ComboBox<String> tmMonthCombo;
    public Button tmCalcBtn;
    public Label tmCalcLabel;

    // Contact schedule report
    public ComboBox<Contact> acCombo;
    public TableView<Appointment> apptContactTable;
    public TableColumn<Appointment, Integer> acIdCol;
    public TableColumn<Appointment, String> acTitleCol;
    public TableColumn<Appointment, String> acDescCol;
    public TableColumn<Appointment, String> acLocCol;
    public TableColumn<Appointment, String> acContactCol;
    public TableColumn<Appointment, String> acTypeCol;
    public TableColumn<Appointment, String> acStartCol;
    public TableColumn<Appointment, String> acEndCol;
    public TableColumn<Appointment, Integer> acCustomerCol;
    public TableColumn<Appointment, Integer> acUserCol;

    // User customer report
    public ComboBox<User> ucUserCombo;
    public ComboBox<Customer> ucCustCombo;
    public Button ucCalcBtn;
    public Label ucCalcLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAppointmentTable();
        initCustomerTable();
        initReportTable();

        tmTypeCombo.setItems(AppointmentDB.getTypes());
        ObservableList<String> months = FXCollections.observableArrayList();
        // lambda expression
        Arrays.stream(Month.values()).forEach(m -> months.add(m.name()));
        tmMonthCombo.setItems(months);
        tmCalcBtn.setOnAction(e -> {
            if (tmTypeCombo.getSelectionModel().getSelectedItem() != null
                    && tmMonthCombo.getSelectionModel().getSelectedItem() != null) {
                int count = AppointmentDB.countByTypeAndMonth(tmTypeCombo.getSelectionModel().getSelectedItem(),
                        Month.valueOf(tmMonthCombo.getSelectionModel().getSelectedItem()));
                if (count == 0) tmCalcLabel.setText("No matching appointments found.");
                else if (count == 1) tmCalcLabel.setText("1 matching appointment found.");
                else tmCalcLabel.setText(count + " matching appointments found.");
            } else tmCalcLabel.setText("Please make your selections.");
        });

        ucUserCombo.setItems(UserDB.getAll());
        ucCustCombo.setItems(CustomerDB.getAll());
        ucCalcBtn.setOnAction(e -> {
            if (ucUserCombo.getSelectionModel().getSelectedItem() != null
                    && ucCustCombo.getSelectionModel().getSelectedItem() != null) {
                int count = AppointmentDB.countByUserAndCustomer(ucUserCombo.getSelectionModel().getSelectedItem(),
                        ucCustCombo.getSelectionModel().getSelectedItem());
                if (count == 0) ucCalcLabel.setText("No matching appointments found.");
                else if (count == 1) ucCalcLabel.setText("1 matching appointment found.");
                else ucCalcLabel.setText(count + " matching appointments found.");
            } else ucCalcLabel.setText("Please make your selections.");
        });

        acCombo.setItems(ContactDB.getAll());
        acCombo.setOnAction(e -> {
            apptContactTable.setItems(AppointmentDB.getByContact(acCombo.getSelectionModel().getSelectedItem().getId()));
            apptContactTable.getSortOrder().add(acIdCol);
        });

        aViewAllRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getAll());
            appointmentTable.getSortOrder().add(aIdCol);
        });

        aViewMonthRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getThisMonth());
            appointmentTable.getSortOrder().add(aIdCol);
        });

        aViewWeekRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getThisWeek());
            appointmentTable.getSortOrder().add(aIdCol);
        });
    }

    public void setTab(int tab) {
        if (tabPane.getTabs().size() > tab) tabPane.getSelectionModel().select(tab);
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

    public void initReportTable() {
        acIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        acTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        acDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        acLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        acContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        acTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        acStartCol.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));
        acEndCol.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));
        acCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        acUserCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
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
        if (CustomerDB.getAll().size() == 0) {
            DialogSender.inform("Schedule Appointment", "There are no customers to schedule appointments with.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Appointment.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateAppointment(ActionEvent actionEvent) {
        if (AppointmentDB.getAll().size() == 0) {
            DialogSender.inform("Update Appointment", "There are no scheduled appointments.");
            return;
        }
        if (appointmentTable.getSelectionModel().isEmpty()) {
            DialogSender.warn("Update Appointment", "Please select the appointment you want to update.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Appointment.fxml"));
            Scene scene = new Scene(loader.load());
            ((AppointmentController)loader.getController()).populate(appointmentTable.getSelectionModel().getSelectedItem());
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCancelAppointment(ActionEvent actionEvent) {
        if (AppointmentDB.getAll().size() == 0) {
            DialogSender.inform("Cancel Appointment", "There are no scheduled appointments.");
            return;
        }
        if (appointmentTable.getSelectionModel().isEmpty()) {
            DialogSender.warn("Cancel Appointment", "Please select the appointment you want to cancel.");
            return;
        }
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        if (DialogSender.confirm("Cancel Appointment", "Are you sure you want to cancel '" + a.getType() + "' appointment at ID " + a.getId() + "?")) {
            if (AppointmentDB.delete(a.getId()) == -1) {
                DialogSender.error("Cancel Appointment", "Could not delete appointment from database!");
                return;
            }
            if (aViewByToggle.getSelectedToggle() == aViewAllRadio) appointmentTable.setItems(AppointmentDB.getAll());
            else if (aViewByToggle.getSelectedToggle() == aViewWeekRadio) appointmentTable.setItems(AppointmentDB.getThisWeek());
            else if (aViewByToggle.getSelectedToggle() == aViewMonthRadio) appointmentTable.setItems(AppointmentDB.getThisMonth());
            appointmentTable.getSortOrder().add(aIdCol);
            DialogSender.inform("Cancel Appointment", "Appointment ID " + a.getId() + " with type '" + a.getType() + "' successfully canceled!");
        }
    }

    public void onAddCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
            Scene scene = new Scene(root);
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
        if (CustomerDB.getAll().size() == 0) {
            DialogSender.inform("Delete Customer", "There are no customers to delete.");
            return;
        }
        if (customerTable.getSelectionModel().isEmpty()) {
            DialogSender.warn("Delete Customer", "Please select the customer you want to delete.");
            return;
        }
        Customer c = customerTable.getSelectionModel().getSelectedItem();
        if (DialogSender.confirm("Delete Customer", "Are you sure you want to delete '" + c.getName() + "' at ID " + c.getId() + "?")) {
            if (AppointmentDB.hasCustomer(c.getId())) {
                DialogSender.warn("Delete Customer", "Please cancel all the customer's appointments first.");
                return;
            }
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

    public void onUpdateCustomer(ActionEvent actionEvent) {
        if (CustomerDB.getAll().size() == 0) {
            DialogSender.inform("Update Customer", "There are no customers to update.");
            return;
        }
        if (customerTable.getSelectionModel().isEmpty()) {
            DialogSender.warn("Update Customer", "Please select the customer you want to update.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Customer.fxml"));
            Scene scene = new Scene(loader.load());
            ((CustomerController)loader.getController()).populate(customerTable.getSelectionModel().getSelectedItem());
            Stage stage = Main.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
