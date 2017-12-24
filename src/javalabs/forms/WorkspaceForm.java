package javalabs.forms;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javalabs.models.Model;

public class WorkspaceForm {

    private Stage stage;
    private Model parent;
    private boolean isEdit;
    private TextField name;
    private TextArea desc;
    private Button saveButton;

    public WorkspaceForm(Model parentContext, boolean editmode){
        this.parent = parentContext;
        this.isEdit = editmode;
    }

    public void init() throws Exception {
        Parent rooter = FXMLLoader.load(getClass().getResource("workspaceform.fxml"));
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
        stage.setScene(scene);
        stage.setResizable(false);
        name  = (TextField) scene.lookup("#name");
        desc  = (TextArea) scene.lookup("#desc");
        saveButton  = (Button) scene.lookup("#saveButton");
        if(isEdit){
            name.setText(parent.getCurrentItem().getName());
            desc.setText(parent.getCurrentItem().getDesc());
        }
        saveButton.setOnMouseClicked(event -> {
            saveForm();
        });
        stage.setTitle(isEdit ? "Редактирование" : "Добавление");
        stage.show();
    }

    private void saveForm(){
        String formName = name.getText();
        String formDesc = desc.getText();
        int status = parent.save(isEdit ? parent.getCurrentItem().getId() : 0, formName, formDesc);
        if(status == 0){
            stage.close();
            parent.refresh();
        }
    }
}
