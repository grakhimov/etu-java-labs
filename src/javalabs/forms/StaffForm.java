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
import java.sql.Ref;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javalabs.classes.Staff;
import javalabs.libraries.Database;
import javalabs.libraries.Images;
import javalabs.libraries.CropImage;
import javalabs.models.StaffModel;
import javalabs.models.StaffModel.*;

import javax.naming.Context;


public class StaffForm{

    private HashMap<String, Integer> divisionMap = new HashMap< String, Integer>();

    private HashMap<String, Integer> positionMap = new HashMap< String, Integer>();

    private StaffModel context;

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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG files", "*.jpg", "*.jpeg", "*.JPG", "*.JPEG");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) photo.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Image image = new Image(file.toURI().toString());
        CropImage cropped = new CropImage(image);
        photo.setImage(cropped.getImageView());
    }

    @FXML
    private void saveForm() throws Exception{
        Stage stage = (Stage) saveButton.getScene().getWindow();
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        int divisionId = divisionMap.get((String) division.getValue());
        int positionId = positionMap.get((String) position.getValue());
        Blob photoStream = Images.imageToMysqlBlob(photo);
        Staff.create(firstName, lastName, divisionId, positionId, photoStream);
        stage.close();
    }


    public void init() throws Exception{
        Parent rooter = FXMLLoader.load(getClass().getResource("staffform.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
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
        for(Object[] row : divisionList){
            // Заполнение строк таблицы
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
        for(Object[] row : positionList){
            // Заполнение строк таблицы
            positionMap.put((String)row[1], (int)row[0]);
            positionsData.add(row[1]);
        }
        position.setItems(positionsData);
    }

}
