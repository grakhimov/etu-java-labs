package javalabs.forms;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javalabs.classes.Card;
import javalabs.models.CardModel;

public class SimpleCard {
    private Button add;
    private TextField cardnumber;
    private Stage stage;
    private CardModel parent;

    public SimpleCard(CardModel parent){
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        Parent rooter;
        try {
            rooter = FXMLLoader.load(getClass().getResource("simplecard.fxml"));
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
        stage.setScene(scene);
        stage.setResizable(false);
        add  = (Button) scene.lookup("#add");
        cardnumber = (TextField) scene.lookup("#cardnumber");
        add.setOnMouseClicked(event -> {
            addCard();
        });
        stage.setTitle("Добавить карту");
        stage.show();
    }


    private void addCard(){
        String cardNumber = cardnumber.getText();
        if(!Card.isUnique(cardNumber)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Такая карта уже есть в системе", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        try {
            Card.addCard(cardNumber);
            parent.refresh();
            stage.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
