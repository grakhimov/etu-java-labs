package javalabs.classes;

import javalabs.libraries.Database;

import javax.swing.text.html.ListView;
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
