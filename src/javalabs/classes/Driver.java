package javalabs.classes;

import javafx.scene.image.ImageView;
import javalabs.libraries.Database;

import java.util.List;

public class Driver {

    private Integer id;
    private String firstName;
    private String lastName;
    private String driverClass;
    private ImageView photo;

    public Driver(Integer id, String firstName, String lastName, String driverClass, ImageView photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.driverClass = driverClass;
        this.photo = photo;
    }

    public Driver() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fio) {
        this.firstName = fio;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public boolean callSetCardStatus(int status) throws Exception {
        if (driverClass == null) {
            return false;
        }
        Card.setCardStatus(driverClass, status);
        return true;
    }

    public boolean cardIsActive() {
        String cardNumber = getDriverClass();
        if (cardNumber == null) {
            return false;
        }
        Database db = new Database();
        String sql = "SELECT is_active FROM cards WHERE card_number = " + cardNumber;
        List<Object[]> result = db.query(sql);
        if (result.size() == 0) {
            return false;
        }
        Object[] row = result.get(0);
        return Integer.parseInt((String) row[0]) == 1;
    }
}
