package javalabs.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardList {
    private Stage stage;
    private ListView<String> cards;
    private Button chooseButton;

    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        Parent rooter = FXMLLoader.load(getClass().getResource("workspaceform.fxml"));
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
        stage.setScene(scene);
        stage.setResizable(false);
        cards  = (ListView) scene.lookup("#cardlist");
        chooseButton  = (Button) scene.lookup("#choose");
        chooseButton.setOnMouseClicked(event -> {
            //saveForm();
        });
        ObservableList<String> items = FXCollections.observableArrayList ("Single", "Double", "Suite", "Family App");
        cards.setItems(items);
        stage.setTitle("Список карт");
        stage.show();
    }
}
