package javalabs.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javalabs.classes.Card;
import javalabs.forms.SimpleCard;
import javalabs.libraries.Database;
import java.util.List;
import java.util.Optional;

public class CardModel {
    private ObservableList<Card> cardData = FXCollections.observableArrayList();
    private TableView<Card> cardTable;
    private TableColumn<Card, String> isActive;
    private TableColumn<Card, String> card;
    private TableColumn<Card, String> cardHolderName;
    private Card currentItem = null;
    /* Связанные кнопки */
    private Button addButton;
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
            Button deleteButton,
            Button toggleCardButton,
            Button unlinkButton){
        this.addButton = addButton;
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
        deleteButton.setOnMouseClicked(this::onDeleteClick);
        toggleCardButton.setOnMouseClicked(this::onToggleCard);
        unlinkButton.setOnMouseClicked(this::onUnlinkCard);
        cardTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (cardTable.getSelectionModel().getSelectedItem() != null) {
                // Текущая выбранная строка таблицы
                currentItem = cardTable.getSelectionModel().getSelectedItem();
                deleteButton.setDisable(false);
                toggleCardButton.setDisable(false);
                unlinkButton.setDisable(false);
            }
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
        SimpleCard form = new SimpleCard(this);
        form.init();
    }

    private void onDeleteClick(MouseEvent event){
        if(currentItem.getCardHolderName() != null){
            ButtonType no = new ButtonType("Отмена", ButtonBar.ButtonData.NO);
            ButtonType yes = new ButtonType("Удалить", ButtonBar.ButtonData.YES);
            Alert alert = new Alert(null, "Эта карта выдана сотруднику: " + currentItem.getCardHolderName() + "\nВсё равно удалить?", no, yes);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get().getText().equals("Удалить")){
                Card.deleteCard(currentItem.getNumber());
                refresh();
            }
            return;
        }
        Card.deleteCard(currentItem.getNumber());
        refresh();

    }

    private void onToggleCard(MouseEvent event){
        if(currentItem.getCardHolderName() != null){
            int status = currentItem.getActive().equals("Активна") ? 1 : 0;
            try {
                Card.setCardStatus(currentItem.getNumber(), status == 1 ? 0 : 1);
            } catch (Exception e){
                e.printStackTrace();
            }
            refresh();
        }
    }

    private void onUnlinkCard(MouseEvent event){
        if(currentItem.getCardHolderName() != null){
            try{
                Card.unlinkCardByNumber(currentItem.getNumber());
            } catch (Exception e){
                e.printStackTrace();
            }
            refresh();
        }
    }

    public void refresh(){
        currentItem = null;
        deleteButton.setDisable(true);
        toggleCardButton.setDisable(true);
        unlinkButton.setDisable(true);
        cardData = FXCollections.observableArrayList();
        init();
    }

}
