package javalabs.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javalabs.classes.Card;
import javalabs.classes.Division;
import javalabs.classes.Position;
import javalabs.classes.Staff;
import javalabs.models.CardModel;
import javalabs.models.DivisionModel;
import javalabs.models.PositionModel;
import javalabs.models.StaffModel;

import java.io.*;
import java.util.Properties;

public class MainWindowController {

    private StaffModel staffModel;
    private DivisionModel divisionModel;
    private PositionModel positionModel;
    private CardModel cardModel;

    // Общие элементы интерфейса
    @FXML
    private Tab StaffofficeTab;

    @FXML
    private Tab IdentifiersTab;

    @FXML
    private Tab StaffTab;

    @FXML
    private Tab BusesTab;

    @FXML
    private Tab RoutesTab;

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

    // Настройки
    @FXML
    private TextField host;

    @FXML
    private TextField port;

    @FXML
    private TextField dbname;

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    @FXML
    private Button applysettings;


    @FXML
    private void initialize() throws Exception{
        // Инициализация разделов
        this.staffModel = new StaffModel(staffTable, addStaffButton, editStaffButton, deleteStaffButton);
        this.divisionModel = new DivisionModel(divisionTable, addDivisionButton, editDivisionButton, deleteDivisionButton);
        this.positionModel = new PositionModel(positionTable, addPositionButton, editPositionButton, deletePositionButton);
        this.cardModel = new CardModel(cardTable, addCardButton, deleteCardButton, toggleCardButton, unlinkButton);
        // Инициализация настроек
        File f = new File("src/javalabs/config.properties");
        InputStream is = new FileInputStream(f);
        Properties p = new Properties();
        p.load(is);
        host.setText(p.getProperty("host"));
        port.setText(p.getProperty("port"));
        dbname.setText(p.getProperty("dbname"));
        user.setText(p.getProperty("user"));
        password.setText(p.getProperty("password"));
        applysettings.setOnMouseClicked(event -> {
            applySettings();
        });
    }

    private void applySettings(){
        try {
            Properties properties = new Properties();
            properties.setProperty("host", host.getText());
            properties.setProperty("port", port.getText());
            properties.setProperty("dbname", dbname.getText());
            properties.setProperty("user", user.getText());
            properties.setProperty("password", password.getText());
            File file = new File("src/javalabs/config.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Stored");
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Обновление разделов при переходе по табам
    public void onTabsClick(){
       staffModel.refresh();
       cardModel.refresh();
    }

}
