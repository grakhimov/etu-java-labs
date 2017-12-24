package javalabs.forms;
import javafx.scene.control.Button;
import javalabs.classes.Division;
import javalabs.classes.Staff;
import javalabs.models.DivisionModel;
import javalabs.models.StaffModel;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;

public class MainWindowController {

    private StaffModel staffModel;
    private DivisionModel divisionModel;

    // Персонал
    @FXML
    private Button addStaffButton;

    @FXML
    private Button editStaffButton;

    @FXML
    private Button deleteStaffButton;

    @FXML
    private TableView<Staff> staffTable;

    // Подразделения
    @FXML
    private Button addDivisionButton;

    @FXML
    private Button editDivisionButton;

    @FXML
    private Button deleteDivisionButton;

    @FXML
    private TableView<Division> divisionTable;

    @FXML
    private void initialize() {
        this.staffModel = new StaffModel(staffTable, addStaffButton, editStaffButton, deleteStaffButton);
        this.divisionModel = new DivisionModel(divisionTable, addDivisionButton, editDivisionButton, deleteDivisionButton);
    }

}
