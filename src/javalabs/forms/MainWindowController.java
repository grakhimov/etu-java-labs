package javalabs.forms;
import javafx.scene.control.Button;
import javalabs.classes.Card;
import javalabs.classes.Division;
import javalabs.classes.Position;
import javalabs.classes.Staff;
import javalabs.models.CardModel;
import javalabs.models.DivisionModel;
import javalabs.models.PositionModel;
import javalabs.models.StaffModel;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;

public class MainWindowController {

    private StaffModel staffModel;
    private DivisionModel divisionModel;
    private PositionModel positionModel;
    private CardModel cardModel;

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

    // Должности
    @FXML
    private Button addPositionButton;

    @FXML
    private Button editPositionButton;

    @FXML
    private Button deletePositionButton;

    @FXML
    private TableView<Position> positionTable;

    //Карты
    @FXML
    private Button addCardButton;

    @FXML
    private Button editCardButton;

    @FXML
    private Button deleteCardButton;

    @FXML
    private Button toggleCardButton;

    @FXML
    private Button unlinkButton;

    @FXML
    private TableView<Card> cardTable;


    @FXML
    private void initialize() {
        this.staffModel = new StaffModel(staffTable, addStaffButton, editStaffButton, deleteStaffButton);
        this.divisionModel = new DivisionModel(divisionTable, addDivisionButton, editDivisionButton, deleteDivisionButton);
        this.positionModel = new PositionModel(positionTable, addPositionButton, editPositionButton, deletePositionButton);
        this.cardModel = new CardModel(cardTable, addCardButton, editCardButton, deleteCardButton, toggleCardButton, unlinkButton);
    }

}
