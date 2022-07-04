import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    public Tab appointmentTab;
    public TableView appointmentTable;
    public RadioButton aViewAllRadio;
    public ToggleGroup aViewByToggle;
    public RadioButton aViewMonthRadio;
    public RadioButton aViewWeekRadio;
    public Button aScheduleBtn;
    public Button aModifyBtn;
    public Button aDeleteBtn;

    public Tab customerTab;
    public TableView customerTable;
    public Button cAddBtn;
    public Button cDeleteBtn;
    public Button cModifyBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onAddAppointment(ActionEvent mouseEvent) {
        
    }

    public void onModifyAppointment(ActionEvent mouseEvent) {

    }

    public void onDeleteAppointment(ActionEvent mouseEvent) {

    }

    public void onAddCustomer(ActionEvent mouseEvent) {

    }

    public void onDeleteCustomer(ActionEvent mouseEvent) {

    }

    public void onModifyCustomer(ActionEvent mouseEvent) {

    }
}
