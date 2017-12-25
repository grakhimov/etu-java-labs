package javalabs.classes;

import javalabs.libraries.Database;

import java.util.List;

public class Card {
    private int id;
    private String number;
    private int cardHolderId;
    private String cardHolderName;
    private boolean active;

    public Card(int id, String number, int cardHolderId, boolean isActive){
        this.id = id;
        this.number = number;
        this.cardHolderId = cardHolderId;
        this.active = isActive;
        this.cardHolderName = fetchCardHolderName();
    }

    public String getNumber() {
        return number;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public int getCardHolderId() {
        return cardHolderId;
    }

    public String getActive(){
        return active ? "Активна" : "Не активна";
    }

    private String fetchCardHolderName(){
        Database db = new Database();
        List<Object[]> result = db.query(
                "SELECT firstname, lastname FROM staff INNER JOIN cards ON staff.id = cards.staff_id AND cards.id = " + id
        );
        if(result.size() > 0){
            Object[] row = result.get(0);
            return row[0] + " " + row[1];
        }
        return null;
    }
}
