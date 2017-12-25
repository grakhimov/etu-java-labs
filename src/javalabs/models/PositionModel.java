package javalabs.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javalabs.classes.Position;
import javalabs.forms.WorkspaceForm;
import javalabs.libraries.Database;

import java.util.List;
import java.util.Optional;

public class PositionModel extends Model{
    private ObservableList<Position> positionData = FXCollections.observableArrayList();
    private TableView<Position> positionTable;
    private TableColumn<Position, String> positionName;
    private TableColumn<Position, String> positionDesc;
    private Position currentItem = null;
    /* Связанные кнопки */
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    public Position getCurrentItem() {
        return currentItem;
    }

    @SuppressWarnings("unchecked")
    public PositionModel(
            TableView<Position> positionTable,
            Button addButton,
            Button editButton,
            Button deleteButton){
        this.addButton = addButton;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.positionTable  = positionTable;
        this.positionName   = (TableColumn<Position, String>) positionTable.getColumns().get(0);
        this.positionDesc   = (TableColumn<Position, String>) positionTable.getColumns().get(1);
        init();
    }

    // Инициализация
    private void init() {
        // Получение данных из базы
        initData();
        // Установка типов содержимого столбцов
        positionName.setCellValueFactory(new PropertyValueFactory<Position, String>("name"));
        positionDesc.setCellValueFactory(new PropertyValueFactory<Position, String>("desc"));
        positionTable.setItems(positionData);
        // Обработчики событий
        addButton.setOnMouseClicked(this::onAddClick);
        editButton.setOnMouseClicked(this::onEditClick);
        deleteButton.setOnMouseClicked(this::onDeleteClick);
        positionTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (positionTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = positionTable.getSelectionModel().getSelectedItem();
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            }
        });
        positionTable.setRowFactory( tv -> {
            TableRow<Position> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    onEditClick(null);
                    //Staff rowData = row.getItem();
                    //System.out.println(rowData);
                }
            });
            return row;
        });
    }

    private void initData() {
        // Получение данных из базы
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT id, position_name, description FROM positions"
        );
        int size = result.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = result.get(i);
            Position position = new Position((Integer) row[0], (String) row[1], (String) row[2]);
            positionData.add(position);
        }
    }

    @Override
    public void refresh(){
        currentItem = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        positionData = FXCollections.observableArrayList();
        init();
    }

    @Override
    public int save(int id, String name, String desc){
        if(name.length() == 0){
            Alert nameFound = new Alert(null, "Не указано название должности", ButtonType.CLOSE);
            nameFound.showAndWait();
            return -1;
        }
        int ids = Position.isUnique(name);
        if(ids > 0 && ids != id){
            Alert unique = new Alert(null, "Должность с таким названием уже существует", ButtonType.CLOSE);
            unique.showAndWait();
            return -1;
                }
        if(id == 0){
            try {
                Position.create(name, desc);
                return 0;
            } catch(Exception e){
                e.printStackTrace();
                return -1;
            }
        }
        try {
            Position.update(id, name, desc);
            return 0;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
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
        String use = Position.checkUse(currentItem.getId());
        if(use.length() > 0){
            String error = "Должность не может быть удалена так как используется сотрудниками: \n" + use;
            Alert inuse = new Alert(null, error, ButtonType.CLOSE);
            inuse.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            inuse.showAndWait();
            return;
        }
        ButtonType no = new ButtonType("Отмена", ButtonBar.ButtonData.NO);
        ButtonType yes = new ButtonType("Удалить", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(null, "Вы действительно хотите удалить должность", no, yes);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getText().equals("Удалить")){
            Position.delete(currentItem.getId());
            refresh();
        }
    }
}
