package javalabs.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.io.File;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javalabs.classes.Staff;
import javalabs.libraries.Database;
import javalabs.libraries.Images;


public class StaffForm {

    private HashMap<String, Integer> divisionMap = new HashMap< String, Integer>();

    private HashMap<String, Integer> positionMap = new HashMap< String, Integer>();

    @FXML
    private ImageView photo;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private ComboBox division;

    @FXML
    private ComboBox position;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        putDivisions();
        putPositions();
    }

    @FXML
    private void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG files (.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) photo.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Image image = new Image(file.toURI().toString());
        photo.setImage(image);
    }

    @FXML
    private void saveForm() throws Exception{
        /*Alert alert = new Alert(null,
                "Вы неслыханный простофиля!",
                ButtonType.APPLY);
        Optional<ButtonType> result = alert.showAndWait();*/

        Stage stage = (Stage) saveButton.getScene().getWindow();
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        int divisionId = divisionMap.get((String) division.getValue());
        int positionId = positionMap.get((String) position.getValue());
        Blob photoStream = Images.imageToBlob(photo);
        Staff.create(firstName, lastName, divisionId, positionId, photoStream);
        stage.close();
    }

    public void init() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("staffform.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Добавление сотрудника");
        stage.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void putDivisions(){
        ObservableList divisionsData = FXCollections.observableArrayList();
        Database db = new Database();
        List<Object[]> divisionList = db.query("SELECT id, division_name FROM divisions");
        int size = divisionList.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = divisionList.get(i);
            divisionMap.put((String)row[1], (Integer)row[0]);
            divisionsData.add(row[1]);
        }
        division.setItems(divisionsData);
    }

    @SuppressWarnings("unchecked")
    private void putPositions(){
        ObservableList positionsData = FXCollections.observableArrayList();
        Database db = new Database();
        List<Object[]> positionList = db.query("SELECT id, position_name FROM positions");
        int size = positionList.size();
        for(int i = 0; i < size; i++){
            // Заполнение строк таблицы
            Object[] row = positionList.get(i);
            positionMap.put((String)row[1], (int)row[0]);
            positionsData.add(row[1]);
        }
        position.setItems(positionsData);
    }

}
