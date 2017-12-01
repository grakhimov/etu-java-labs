package javalabs.classes;

import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javalabs.libraries.Database;

public class Staff extends User{
    private String division;
    private String position;

    public Staff(int id, String firstName, String lastName, String division, String position, String cardNumber, ImageView photo) {
        super(id, firstName, lastName, cardNumber, photo);
        this.division = division;
        this.position = position;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public static int create(String firstName, String lastName, Integer divisionId, Integer positionId, Blob photo) throws Exception{
        InputStream inputStream = photo.getBinaryStream();
        String sql = "INSERT INTO staff (firstname, lastname, division_id, position_id, photo) values (?, ?, ?, ?, ?)";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setInt(3, divisionId);
        ps.setInt(4, positionId);
        ps.setBlob(5, inputStream);
        if(ps.executeUpdate() > 0){
            connect.close();
            return 1;
        }
        connect.close();
        return 0;
    }

}
