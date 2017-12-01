package javalabs.forms;

import javafx.scene.control.Button;
import javalabs.classes.Staff;
import javalabs.models.StaffModel;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;

public class MainWindowController {

    StaffForm staffWindow;
    StaffModel staffModel;

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
        this.staffModel= new StaffModel(staffTable, editStaffButton, deleteStaffButton);
    }

    public void addClick() throws Exception{
        staffWindow = new StaffForm();
        staffWindow.init();
        staffModel.refresh();
    }

    public void editClick(){
        System.out.println("Редактируем...");
    }

    public void removeClick(){
        System.out.println("Удаляем...");
    }


}
