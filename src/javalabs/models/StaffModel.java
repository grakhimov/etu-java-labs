package javalabs.models;

import javalabs.classes.Staff;
import javalabs.libraries.Database;
import java.util.List ;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.image.*;


public class StaffModel {
    private ObservableList<Staff> staffData = FXCollections.observableArrayList();

    // Таблица и основные колонки
    private TableView<Staff> staffTable;
    // Имя
    private TableColumn<Staff, String> firstName;
    // Фамилия
    private TableColumn<Staff, String> lastName;
    // Подразделение
    private TableColumn<Staff, String> division;
    // Должность
    private TableColumn<Staff, String> position;
    // Номер карты
    private TableColumn<Staff, String> cardNumber;
    // Фотография
    private TableColumn<Staff, ImageView> photo;

    /* Связанные кнопки */
    private Button editButton;
    private Button deleteButton;

    // Инициализация
    private void init() {
        // Получение данных из базы
        initData();
        // Установка типов содержимого столбцов
        firstName.setCellValueFactory(new PropertyValueFactory<Staff, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Staff, String>("lastName"));
        division.setCellValueFactory(new PropertyValueFactory<Staff, String>("division"));
        position.setCellValueFactory(new PropertyValueFactory<Staff, String>("position"));
        cardNumber.setCellValueFactory(new PropertyValueFactory<Staff, String>("cardNumber"));
        photo.setCellValueFactory(new PropertyValueFactory<Staff, ImageView>("photo"));
        staffTable.setItems(staffData);
    }

    @SuppressWarnings("unchecked")
    public StaffModel(TableView<Staff> staffTable, Button editButton, Button deleteButton){
        // Связанные кнопки
        this.editButton = editButton;
        this.deleteButton = deleteButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.staffTable     = staffTable;
        this.firstName      = (TableColumn<Staff, String>) staffTable.getColumns().get(0);
        this.lastName       = (TableColumn<Staff, String>) staffTable.getColumns().get(1);
        this.division       = (TableColumn<Staff, String>) staffTable.getColumns().get(2);
        this.position       = (TableColumn<Staff, String>) staffTable.getColumns().get(3);
        this.cardNumber     = (TableColumn<Staff, String>) staffTable.getColumns().get(4);
        this.photo          = (TableColumn<Staff, ImageView>) staffTable.getColumns().get(5);
        this.staffTable.setOnMouseClicked(this::onSelectRow);
        init(); // Инициализация
    }

    private void initData() {
        // Получение данных из базы
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT  staff.id, firstname, lastname, division_name, position_name, card_number, photo from staff\n" +
                        "  LEFT JOIN divisions\n" +
                        "  ON staff.division_id = divisions.id\n" +
                        "  LEFT JOIN positions\n" +
                        "  ON staff.position_id = positions.id"
        );
        int size = result.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = result.get(i);
            Staff staff = new Staff((Integer) row[0], (String) row[1], (String) row[2], (String) row[3], (String) row[4], (String) row[5], (ImageView) row[6]);
            staffData.add(staff);
        }
    }

    private void onSelectRow(javafx.scene.input.MouseEvent event){
        Staff item = staffTable.getSelectionModel().getSelectedItem();
        if(item != null){
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public void refresh(){
        staffData = FXCollections.observableArrayList();
        init();
    }
}
