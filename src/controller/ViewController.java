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

/**
 * The controller for the View screen.
 *
 * @author Alex Bright
 */
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

    /**
     * Initializes the ViewController.
     * This method calls init methods for each table (appt, customer, report),
     * fills month report ComboBox, and defines action methods for various functionalities.
     * <p>
     *     <b>Lambda expression:</b> I am using an Array stream on the Month values array so that I can use
     *     the forEach lambda expression. This lambda expression fills the months ObservableList that will be
     *     used for the month report ComboBox data. The use of the lambda expression allows for much more readable
     *     and more efficient code. It is more readable because it simplifies the for-each loop into a single line
     *     that can be read like a normal line of words.
     *     ("In the stream of month values, each month's name will be added to the list of months.")
     * </p>
     * <p>
     *     The remaining lambda expressions define functionalities for various GUI items.
     *     These functionalities include:
     *     <ul>
     *         <li>Type/Month Calculation Button</li>
     *         <li>User/Customer Calculation Button</li>
     *         <li>Appointments by Contact ComboBox</li>
     *         <li>Each View Radio Button for Appointments (all, month, week)</li>
     *         <li>Report tab selection (to reset report selections)</li>
     *     </ul>
     * </p>
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAppointmentTable();
        initCustomerTable();
        initReportTable();

        ObservableList<String> months = FXCollections.observableArrayList();
        // lambda expression
        Arrays.stream(Month.values()).forEach(m -> months.add(m.name()));
        tmMonthCombo.setItems(months);

        // lambda expression
        tmCalcBtn.setOnAction(e -> {
            if (tmTypeCombo.getSelectionModel().getSelectedItem() != null
                    && tmMonthCombo.getSelectionModel().getSelectedItem() != null) {
                int count = AppointmentDB.countByTypeAndMonth(tmTypeCombo.getSelectionModel().getSelectedItem(),
                        Month.valueOf(tmMonthCombo.getSelectionModel().getSelectedItem()));
                if (count == 0) tmCalcLabel.setText("No matching appointments found.");
                else if (count == 1) tmCalcLabel.setText("1 matching appointment found.");
                else tmCalcLabel.setText(count + " matching appointments found.");
            } else tmCalcLabel.setText("Please select type and month.");
        });

        // lambda expression
        ucCalcBtn.setOnAction(e -> {
            if (ucUserCombo.getSelectionModel().getSelectedItem() != null
                    && ucCustCombo.getSelectionModel().getSelectedItem() != null) {
                int count = AppointmentDB.countByUserAndCustomer(ucUserCombo.getSelectionModel().getSelectedItem(),
                        ucCustCombo.getSelectionModel().getSelectedItem());
                if (count == 0) ucCalcLabel.setText("No matching appointments found.");
                else if (count == 1) ucCalcLabel.setText("1 matching appointment found.");
                else ucCalcLabel.setText(count + " matching appointments found.");
            } else ucCalcLabel.setText("Please select user and customer.");
        });

        // lambda expression
        acCombo.setOnAction(e -> {
            if (acCombo.getSelectionModel().getSelectedItem() != null) {
                apptContactTable.setItems(AppointmentDB.getByContact(acCombo.getSelectionModel().getSelectedItem().getId()));
                apptContactTable.getSortOrder().add(acIdCol);
            }
        });

        // lambda expression
        aViewAllRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getAll());
            appointmentTable.getSortOrder().add(aIdCol);
        });

        // lambda expression
        aViewMonthRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getThisMonth());
            appointmentTable.getSortOrder().add(aIdCol);
        });

        // lambda expression
        aViewWeekRadio.setOnAction(e -> {
            appointmentTable.setItems(AppointmentDB.getThisWeek());
            appointmentTable.getSortOrder().add(aIdCol);
        });

        // lambda expression
        reportTab.setOnSelectionChanged(e -> {
            if (reportTab.isSelected()) resetReportTab();
        });
    }

    /**
     * Open desired tab in the View screen.
     * If tab param is valid (non-negative and less than size of tab list),
     * set as selected tab.
     *
     * @param tab index of tab to select
     */
    public void setTab(int tab) {
        if (tab >= 0 && tabPane.getTabs().size() > tab) tabPane.getSelectionModel().select(tab);
    }

    /**
     * Initializes main appointment table.
     * This method sets table columns to corresponding values
     * and displays all appointments by default, sorted by ID.
     */
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

    /**
     * Initializes appointment by contact report table.
     * This method sets table columns to corresponding values.
     */
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

    /**
     * Resets report tab fields.
     * This method fills the combo boxes, clears
     * calculation labels, and clears appointments by contacts
     * table.
     */
    public void resetReportTab() {
        tmTypeCombo.setItems(AppointmentDB.getTypes());
        tmCalcLabel.setText("");

        ucUserCombo.setItems(UserDB.getAll());
        ucCustCombo.setItems(CustomerDB.getAll());
        ucCalcLabel.setText("");

        acCombo.setItems(ContactDB.getAll());
        apptContactTable.getItems().clear();
    }

    /**
     * Initializes main customer table.
     * This method sets table columns to corresponding values
     * and displays all customers, sorted by ID.
     */
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

    /**
     * On button click, switches to schedule appointment form.
     * This method validates selection and launches Appointment form.
     *
     * @param actionEvent
     */
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

    /**
     * On button click, switches to update appointment form.
     * This method validates selection and calls populate() method inside controller
     * to pre-populate all fields with selected appointment information.
     *
     * @param actionEvent
     */
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

    /**
     * On button click, cancel selected appointment.
     * This method validates selection and attempts to delete from the database.
     * On successful deletion, refresh appointment table and display proper dialog box.
     *
     * @param actionEvent
     */
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

    /**
     * On button click, switches to customer registration form.
     *
     * @param actionEvent
     */
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

    /**
     * On button click, switch to customer update form.
     * This method validates selection and calls populate() method inside controller
     * to pre-populate all fields with selected customer information.
     *
     * @param actionEvent
     */
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

    /**
     * On button click, delete selected customer.
     * This method validates selection and checks customer's appointments,
     * then attempts to delete customer from database.
     * On successful deletion, refresh customer table and display proper dialog box.
     *
     * @param actionEvent
     */
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

}
