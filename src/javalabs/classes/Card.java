package javalabs.classes;

import javalabs.libraries.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public static String[] getAllUnuseCards(){
        Database db = new Database();
        List<Object[]> result = db.query(
                "SELECT card_number FROM cards WHERE staff_id IS NULL"
        );
        String[] cardArray = new String[result.size()];
        if(result.size() > 0){
            for(int i = 0; i < result.size(); i++){
                Object[] row = result.get(i);
                cardArray[i] = (String)row[0];
            }
        }
        return cardArray;
    }

    public static int linkCardByStaffId(int staff_id, String cardnumber) throws Exception{
        Database db = new Database();
        String sql = "UPDATE cards SET staff_id = " + staff_id +" WHERE card_number = " + cardnumber;
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        if(ps.executeUpdate() > 0){
            connect.close();
            return -1;
        }
        connect.close();
        return 0;
    }

    public static int unlinkCardByNumber(String number) throws Exception{
        Database db = new Database();
        String sql = "UPDATE cards SET staff_id = NULL, is_active = 0 WHERE card_number = " + number;
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        if(ps.executeUpdate() > 0){
            connect.close();
            return -1;
        }
        connect.close();
        return 0;
    }

    public static int addCard(String cardNumber) throws Exception{
        Database db = new Database();
        String sql = "INSERT INTO cards (card_number) VALUES ('" + cardNumber + "')";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        if(ps.executeUpdate() > 0){
            connect.close();
            return 1;
        }
        connect.close();
        return 0;
    }

    public static int deleteCard(String cardNumber){
        String sql = "DELETE FROM cards WHERE card_number = " + cardNumber;
        Database db = new Database();
        try{
            db.update(sql);
            return 0;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isUnique(String cardNumber){
        Database db = new Database();
        List<Object[]> result = db.query("SELECT card_number FROM cards WHERE card_number = " + cardNumber);
        if(result.size() > 0){
            return false;
        }
        return true;
    }

    public static boolean setCardStatus(String cardNumber, int status) throws Exception{
        if(cardNumber == null){
            return false;
        }
        Database db = new Database();
        String sql = "UPDATE cards SET is_active = " + status + " WHERE card_number = " + cardNumber;
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        if(ps.executeUpdate() > 0){
            connect.close();
            return false;
        }
        connect.close();
        return true;
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
