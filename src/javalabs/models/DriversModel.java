package javalabs.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javalabs.classes.Driver;
import javalabs.classes.Staff;
import javalabs.forms.StaffForm;
import javalabs.libraries.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class DriversModel {
    private ObservableList<Driver> driversData = FXCollections.observableArrayList();
    // Таблица и основные колонки
    private TableView<Driver> driversTable;
    // Имя
    private TableColumn<Driver, String> firstName;
    // Фамилия
    private TableColumn<Driver, String> lastName;
    // Класс водителя
    private TableColumn<Driver, String> driverClass;
    // Опыт
    private TableColumn<Driver, Integer> experience;
    // Зарплата
    private TableColumn<Driver, Double> salary;
    // Нарушения
    private TableColumn<Driver, HashMap> violations;
    // Текущая выбранная строка таблицы
    private Driver currentItem = null;
    /* Связанные кнопки */
    private Button addStaffButton;
    private Button editStaffButton;
    private Button deleteStaffButton;

    @SuppressWarnings("unchecked")
    public DriversModel(TableView<Driver> driversTable, Button addStaffButton, Button editButton, Button deleteButton) {
        // Связанные кнопки
        this.addStaffButton = addStaffButton;
        this.editStaffButton = editButton;
        this.deleteStaffButton = deleteButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.driversTable = driversTable;
        this.firstName = (TableColumn<Driver, String>) driversTable.getColumns().get(0);
        this.lastName = (TableColumn<Driver, String>) driversTable.getColumns().get(1);
        this.division = (TableColumn<Driver, String>) driversTable.getColumns().get(2);
        this.position = (TableColumn<Driver, String>) driversTable.getColumns().get(3);
        this.cardNumber = (TableColumn<Driver, String>) driversTable.getColumns().get(4);
        this.photo = (TableColumn<Driver, ImageView>) driversTable.getColumns().get(5);
        driversTable.setRowFactory(tv -> {
            TableRow<Driver> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    onEditClick(null);
                }
            });
            return row;
        });
        init(); // Инициализация
    }

    public Driver getCurrentItem() {
        return currentItem;
    }

    // Инициализация
    private void init() {
        // Получение данных из базы
        initData();
        // Установка типов содержимого столбцов
        firstName.setCellValueFactory(new PropertyValueFactory<Driver, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Driver, String>("lastName"));
        driverClass.setCellValueFactory(new PropertyValueFactory<Driver, String>("division"));
        experience.setCellValueFactory(new PropertyValueFactory<Driver, String>("position"));
        cardNumber.setCellValueFactory(new PropertyValueFactory<Driver, String>("cardNumber"));
        photo.setCellValueFactory(new PropertyValueFactory<Driver, ImageView>("photo"));
        driversTable.setItems(driversData);
        // Обработчики событий
        addStaffButton.setOnMouseClicked(this::onAddClick);
        editStaffButton.setOnMouseClicked(this::onEditClick);
        deleteStaffButton.setOnMouseClicked(this::onDeleteClick);
        driversTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (driversTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = driversTable.getSelectionModel().getSelectedItem();
                editStaffButton.setDisable(false);
                deleteStaffButton.setDisable(false);
            }
        });
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
            ImageView rs = (ImageView) row[6];
            rs.setFitWidth(80);
            rs.setFitHeight(80);
            Driver driver = new Driver((Integer) row[0], (String) row[1], (String) row[2], (String) row[3], (String) row[4], (String) row[5], rs);
            driversData.add(driver);
        }
    }

    public void refresh(){
        currentItem = null;
        editStaffButton.setDisable(true);
        deleteStaffButton.setDisable(true);
        driversData = FXCollections.observableArrayList();
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
