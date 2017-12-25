package javalabs.models;

import javalabs.forms.StaffForm;
import javafx.scene.control.*;
import javalabs.classes.Staff;
import javalabs.libraries.Database;
import java.util.List ;
import java.util.Optional;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;


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
    // Текущая выбранная строка таблицы
    private Staff currentItem = null;
    /* Связанные кнопки */
    private Button addStaffButton;
    private Button editStaffButton;
    private Button deleteStaffButton;

    public Staff getCurrentItem() {
        return currentItem;
    }

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
        // Обработчики событий
        addStaffButton.setOnMouseClicked(this::onAddClick);
        editStaffButton.setOnMouseClicked(this::onEditClick);
        deleteStaffButton.setOnMouseClicked(this::onDeleteClick);
        staffTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (staffTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = staffTable.getSelectionModel().getSelectedItem();
                editStaffButton.setDisable(false);
                deleteStaffButton.setDisable(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public StaffModel(TableView<Staff> staffTable, Button addStaffButton, Button editButton, Button deleteButton){
        // Связанные кнопки
        this.addStaffButton = addStaffButton;
        this.editStaffButton = editButton;
        this.deleteStaffButton = deleteButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.staffTable     = staffTable;
        this.firstName      = (TableColumn<Staff, String>) staffTable.getColumns().get(0);
        this.lastName       = (TableColumn<Staff, String>) staffTable.getColumns().get(1);
        this.division       = (TableColumn<Staff, String>) staffTable.getColumns().get(2);
        this.position       = (TableColumn<Staff, String>) staffTable.getColumns().get(3);
        this.cardNumber     = (TableColumn<Staff, String>) staffTable.getColumns().get(4);
        this.photo          = (TableColumn<Staff, ImageView>) staffTable.getColumns().get(5);
        staffTable.setRowFactory( tv -> {
            TableRow<Staff> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    onEditClick(null);
                    //Staff rowData = row.getItem();
                    //System.out.println(rowData);
                }
            });
            return row;
        });
        init(); // Инициализация
    }

    private void initData() {
        // Получение данных из базы
        Database example = new Database();
        String sql = "SELECT  staff.id, firstname, lastname, division_name, position_name, card_number, photo from staff\n" +
                "  LEFT JOIN divisions\n" +
                "  ON staff.division_id = divisions.id\n" +
                "  LEFT JOIN positions\n" +
                "  ON staff.position_id = positions.id" +
                "  LEFT JOIN cards ON cards.staff_id = staff.id";
        List<Object[]> result = example.query(sql);
        int size = result.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = result.get(i);
            Staff staff = new Staff((Integer) row[0], (String) row[1], (String) row[2], (String) row[3], (String) row[4], (String) row[5], (ImageView) row[6]);
            staffData.add(staff);
        }
    }

    public void refresh(){
        currentItem = null;
        editStaffButton.setDisable(true);
        deleteStaffButton.setDisable(true);
        staffData = FXCollections.observableArrayList();
        init();
    }

    private void onAddClick(MouseEvent event){
        StaffForm window = new StaffForm(this, false);
        try {
            window.init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onEditClick(MouseEvent event){
        StaffForm window = new StaffForm(this, true);
        try {
            window.init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onDeleteClick(MouseEvent event){
        ButtonType no = new ButtonType("Отмена", ButtonBar.ButtonData.NO);
        ButtonType yes = new ButtonType("Удалить", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(null, "Вы действительно хотите удалить сотрудника", no, yes);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getText().equals("Удалить")){
            if(Staff.delete(currentItem.getId()) == 0){
                refresh();
                return;
            }
            Alert delError = new Alert(null, "Что-то пошло не так...", ButtonType.CLOSE);
            delError.showAndWait();
        }
    }
}
