package javalabs.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javalabs.classes.Card;

public class CardList {
    private Stage stage;
    private ListView<String> cards;
    private Button chooseButton;
    private String currentItem = null;
    private StaffForm parent;

    public CardList(StaffForm parent){
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        Parent rooter;
        try {
            rooter = FXMLLoader.load(getClass().getResource("cardlist.fxml"));
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
        stage.setScene(scene);
        stage.setResizable(false);
        cards  = (ListView) scene.lookup("#cardlist");
        cards.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentItem = newValue;
                chooseButton.setDisable(false);
            }
        });
        chooseButton  = (Button) scene.lookup("#choose");
        chooseButton.setOnMouseClicked(event -> {
            setCard();
        });
        ObservableList<String> items = FXCollections.observableArrayList();
        String[] cardlist = Card.getAllUnuseCards();
        items.addAll(cardlist);
        cards.setItems(items);
        stage.setTitle("Список доступных карт");
        stage.show();
    }

    private void setCard(){
        TextField parentCardField = parent.getCardNumber();
        String currentStaffCard = parentCardField.getText();
        if(currentStaffCard != null) {
            try{
                Card.unlinkCardByNumber(currentStaffCard);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        parentCardField.setText(currentItem);
        int staff_id = parent.getStaffModel().getCurrentItem().getId();
        try{
            Card.linkCardByStaffId(staff_id, currentItem);
            parent.getStaffModel().getCurrentItem().setDriverClass(currentItem);
        } catch(Exception e){
            e.printStackTrace();
        }
        stage.close();
    }
}
