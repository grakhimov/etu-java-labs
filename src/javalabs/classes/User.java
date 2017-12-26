package javalabs.classes;

import javafx.scene.image.*;
import javalabs.libraries.Database;

import java.util.List;

public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private ImageView photo;

    public User(Integer id, String firstName, String lastName, String cardNumber, ImageView photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.photo = photo;
    }

    public User() {
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public boolean callSetCardStatus(int status) throws Exception {
        if (cardNumber == null) {
            return false;
        }
        Card.setCardStatus(cardNumber, status);
        return true;
    }

    public boolean cardIsActive() {
        String cardNumber = getCardNumber();
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
