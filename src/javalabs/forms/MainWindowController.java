package javalabs.forms;
import javafx.scene.control.Button;
import javalabs.classes.Staff;
import javalabs.models.StaffModel;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;

public class MainWindowController {

    private StaffModel staffModel;

    @FXML
    private Button addStaffButton;

    @FXML
    private Button editStaffButton;

    @FXML
    private Button deleteStaffButton;

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private void initialize() {
        this.staffModel = new StaffModel(staffTable, addStaffButton, editStaffButton, deleteStaffButton);
    }

}
