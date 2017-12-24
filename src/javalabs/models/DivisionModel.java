package javalabs.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javalabs.classes.Division;
import javalabs.forms.WorkspaceForm;
import javalabs.libraries.Database;

import java.util.List;
import java.util.Optional;


public class DivisionModel extends Model{
    private ObservableList<Division> divisionData = FXCollections.observableArrayList();
    private TableView<Division> divisionTable;
    private TableColumn<Division, String> divisionName;
    private TableColumn<Division, String> divisionDesc;
    private Division currentItem = null;
    /* Связанные кнопки */
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    public Division getCurrentItem() {
        return currentItem;
    }

    @SuppressWarnings("unchecked")
    public DivisionModel(
            TableView<Division> divisionTable,
            Button addButton,
            Button editButton,
            Button deleteButton){
        this.addButton = addButton;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.divisionTable  = divisionTable;
        this.divisionName   = (TableColumn<Division, String>) divisionTable.getColumns().get(0);
        this.divisionDesc   = (TableColumn<Division, String>) divisionTable.getColumns().get(1);
        init();
    }

    // Инициализация
    private void init() {
        // Получение данных из базы
        initData();
        // Установка типов содержимого столбцов
        divisionName.setCellValueFactory(new PropertyValueFactory<Division, String>("name"));
        divisionDesc.setCellValueFactory(new PropertyValueFactory<Division, String>("desc"));
        divisionTable.setItems(divisionData);
        // Обработчики событий
        addButton.setOnMouseClicked(this::onAddClick);
        editButton.setOnMouseClicked(this::onEditClick);
        deleteButton.setOnMouseClicked(this::onDeleteClick);
        divisionTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (divisionTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = divisionTable.getSelectionModel().getSelectedItem();
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            }
        });
    }

    private void initData() {
        // Получение данных из базы
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT id, division_name, description FROM divisions"
        );
        int size = result.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = result.get(i);
            Division division = new Division((Integer) row[0], (String) row[1], (String) row[2]);
            divisionData.add(division);
        }
    }

    @Override
    public void refresh(){
        currentItem = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        divisionData = FXCollections.observableArrayList();
        init();
    }

    @Override
    public int save(int id, String name, String desc){
        if(name.length() == 0){
            Alert nameFound = new Alert(null, "Не указано название подразделения", ButtonType.CLOSE);
            nameFound.showAndWait();
            return -1;
        }
        if(id == 0){
            try {
                Division.create(name, desc);
            } catch(Exception e){
                e.printStackTrace();
            }
            return -1;
        }
        try {
            Division.update(id, name, desc);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void onAddClick(MouseEvent event){
        WorkspaceForm window = new WorkspaceForm(this, false);
        try {
            window.init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onEditClick(MouseEvent event){
        WorkspaceForm window = new WorkspaceForm(this, true);
        try {
            window.init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onDeleteClick(MouseEvent event){
        String use = Division.checkUse(currentItem.getId());
        if(use.length() > 0){
            String error = "Подразделение не может быть удалено так как в нем числятся сотрудники: \n" + use;
            Alert inuse = new Alert(null, error, ButtonType.CLOSE);
            inuse.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            inuse.showAndWait();
            return;
        }
        ButtonType no = new ButtonType("Отмена", ButtonBar.ButtonData.NO);
        ButtonType yes = new ButtonType("Удалить", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(null, "Вы действительно хотите удалить подразделение", no, yes);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getText().equals("Удалить")){
            Division.delete(currentItem.getId());
            refresh();
        }
    }
}
