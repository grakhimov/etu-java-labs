package javalabs.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javalabs.classes.Staff;
import javalabs.libraries.Database;
import javalabs.libraries.Images;
import javalabs.libraries.CropImage;
import javalabs.models.StaffModel;


public class StaffForm{

    private HashMap<String, Integer> divisionMap = new HashMap< String, Integer>();

    private HashMap<String, Integer> positionMap = new HashMap< String, Integer>();

    private StaffModel staffModel;

    private ImageView photo;

    private TextField firstname;

    private TextField lastname;

    private TextField cardNumber;

    private ToggleButton activate;

    private ComboBox division;

    private ComboBox position;

    private Button saveButton;

    private Button uploadPhoto;

    private Button addCardButton;

    private Staff currentStaff;

    private boolean isEdit;

    public StaffForm(StaffModel parentContext, boolean editmode){
        this.isEdit = editmode;
        this.staffModel = parentContext;
        this.currentStaff = parentContext.getCurrentItem();
    }

    public TextField getCardNumber() {
        return cardNumber;
    }

    public StaffModel getStaffModel() {
        return staffModel;
    }

    private void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG files", "*.jpg", "*.jpeg", "*.JPG", "*.JPEG");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) photo.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Image image;
        try{
            image = new Image(file.toURI().toString());
        } catch (Exception e){
            return;
        }
        CropImage cropped = new CropImage(image);
        if(cropped.getImageView() != null) {
            photo.setImage(cropped.getImageView());
        }
    }

    private void saveForm(){
        Stage stage = (Stage) saveButton.getScene().getWindow();
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        int divisionId;
        try {
            divisionId = divisionMap.get((String) division.getValue());
        } catch (Exception e){
            divisionId = -1;
        }
        int positionId;
        try {
            positionId = positionMap.get((String) position.getValue());
        } catch (Exception e){
            positionId = -1;
        }
        String formErrors = validateForm(firstName, lastName, divisionId, positionId);
        if(formErrors.length() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, formErrors, ButtonType.CLOSE);
            alert.showAndWait();
            return;
        }
        try {
            Blob photoStream = Images.imageToMysqlBlob(photo);
            if(!isEdit) {
                Staff.create(firstName, lastName, divisionId, positionId, photoStream);
            } else Staff.update(currentStaff.getId(), firstName, lastName, divisionId, positionId, photoStream);
        } catch (Exception e){
            e.printStackTrace();
        }
        staffModel.refresh();
        stage.close();
    }

    @SuppressWarnings("unchecked")
    public void init() throws Exception{
        Parent rooter = FXMLLoader.load(getClass().getResource("staffform.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(rooter);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(isEdit ? "Редактирование сотрудника" : "Добавление сотрудника");
        firstname   = (TextField)   scene.lookup("#firstname");
        lastname    = (TextField)   scene.lookup("#lastname");
        cardNumber  = (TextField)   scene.lookup("#cardNumber");
        activate  =   (ToggleButton)scene.lookup("#isActive");
        addCardButton = (Button) scene.lookup("#add");
        division    = (ComboBox)    scene.lookup("#division");
        position    = (ComboBox)    scene.lookup("#position");
        photo       = (ImageView)   scene.lookup("#photo");
        uploadPhoto = (Button)      scene.lookup("#uploadPhoto");
        saveButton  = (Button)      scene.lookup("#saveButton");
        addCardButton.setDisable(true);
        activate.setDisable(true);
        // Обработчики
        uploadPhoto.setOnMouseClicked(event -> {
            chooseFile();
        });
        saveButton.setOnMouseClicked(event -> {
            saveForm();
        });
        addCardButton.setOnMouseClicked(event -> {
            try {openCardList();} catch(Exception e){}
        });
        activate.setOnMouseClicked(event -> {
           toggleCardStatus();
        });
        putDivisions();
        putPositions();
        if(isEdit && currentStaff != null){
            firstname.setText(currentStaff.getFirstName());
            lastname.setText(currentStaff.getLastName());
            cardNumber.setText(currentStaff.getCardNumber());
            division.setValue(currentStaff.getDivision());
            position.setValue(currentStaff.getPosition());
            photo.setImage(currentStaff.getPhoto().getImage());
            boolean isActiveStatus = currentStaff.cardIsActive();
            addCardButton.setDisable(false);
            activate.setDisable(false);
            activate.setSelected(isActiveStatus);
            if(isActiveStatus){
                activate.setText("Активна");
            } else activate.setText("Не активна");
        }
        stage.show();
    }

    private void openCardList() throws Exception{
        CardList cards = new CardList(this);
        cards.init();
    }

    private String validateForm(String firstName, String lastName, Integer divisionId, Integer positionId){
        String errors = "";
        if(firstName.length() == 0){
            errors += "Не указано имя сотрудника!\n";
        }
        if(lastName.length() == 0){
            errors += "Не указана фамилия сотрудника!\n";
        }
        if(divisionId == -1){
            errors += "Не указано подразделение!\n";
        }
        if(positionId == -1){
            errors += "Не указана должность!\n";
        }
        return errors;
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

    private void toggleCardStatus(){
        String card = cardNumber.getText();
        boolean cardStatus = activate.isSelected();
        if(card == null){
            activate.setSelected(false);
            activate.setText("Не активна");
            return;
        }
        if(cardStatus) {
            activate.setText("Активна");
        } else activate.setText("Не активна");
        try{
            currentStaff.callSetCardStatus((cardStatus) ? 1 : 0);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
