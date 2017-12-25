package javalabs.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javalabs.classes.Card;
import javalabs.libraries.Database;
import java.util.List;

public class CardModel {
    private ObservableList<Card> cardData = FXCollections.observableArrayList();
    private TableView<Card> cardTable;
    private TableColumn<Card, String> isActive;
    private TableColumn<Card, String> card;
    private TableColumn<Card, String> cardHolderName;
    private Card currentItem = null;
    /* Связанные кнопки */
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button toggleCardButton;
    private Button unlinkButton;

    public Card getCurrentItem() {
        return currentItem;
    }

    @SuppressWarnings("unchecked")
    public CardModel(
            TableView<Card> cardTable,
            Button addButton,
            Button editButton,
            Button deleteButton,
            Button toggleCardButton,
            Button unlinkButton){
        this.addButton = addButton;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
        this.toggleCardButton = toggleCardButton;
        this.unlinkButton = unlinkButton;
        // Получение объекта таблицы из корневого контроллера и выборка столбцов
        this.cardTable  = cardTable;
        this.isActive   = (TableColumn<Card, String>) cardTable.getColumns().get(0);
        this.card       = (TableColumn<Card, String>) cardTable.getColumns().get(1);
        this.cardHolderName = (TableColumn<Card, String>) cardTable.getColumns().get(2);
        init();
    }

    // Инициализация
    private void init() {
        // Получение данных из базы
        initData();
        // Установка типов содержимого столбцов
        isActive.setCellValueFactory(new PropertyValueFactory<Card, String>("active"));
        card.setCellValueFactory(new PropertyValueFactory<Card, String>("number"));
        cardHolderName.setCellValueFactory(new PropertyValueFactory<Card, String>("cardHolderName"));
        cardTable.setItems(cardData);
        // Обработчики событий
        addButton.setOnMouseClicked(this::onAddClick);
        editButton.setOnMouseClicked(this::onEditClick);
        deleteButton.setOnMouseClicked(this::onDeleteClick);
        toggleCardButton.setOnMouseClicked(this::onToggleCard);
        unlinkButton.setOnMouseClicked(this::onUnlinkCard);
        cardTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (cardTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = cardTable.getSelectionModel().getSelectedItem();
                editButton.setDisable(false);
                deleteButton.setDisable(false);
                toggleCardButton.setDisable(false);
                unlinkButton.setDisable(false);
            }
        });
        cardTable.setRowFactory( tv -> {
            TableRow<Card> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    onEditClick(null);
                }
            });
            return row;
        });
    }

    private void initData() {
        // Получение данных из базы
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT id, card_number, staff_id, is_active FROM cards"
        );
        int size = result.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = result.get(i);
            Card card = new Card((Integer) row[0], (String) row[1], (int) row[2], Integer.parseInt((String)row[3]) == 1);
            cardData.add(card);
        }
    }


    private void onAddClick(MouseEvent event){

    }

    private void onEditClick(MouseEvent event){

    }

    private void onDeleteClick(MouseEvent event){

    }

    private void onToggleCard(MouseEvent event){

    }

    private void onUnlinkCard(MouseEvent event){

    }

    public void refresh(){
        currentItem = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        toggleCardButton.setDisable(true);
        unlinkButton.setDisable(true);
        cardData = FXCollections.observableArrayList();
        init();
    }

}
