package javalabs.classes;

import javalabs.libraries.Database;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

public class Driver {

    private Integer id;
    private String firstName;
    private String lastName;
    private String driverClass;
    private Integer experience;
    private Double salary;
    private HashMap<Integer, String> violations;

    public Driver() {
    }

    public Driver(Integer id, String firstName, String lastName, String driverClass, Integer experience, Double salary, HashMap<Integer, String> violations) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.driverClass = driverClass;
        this.experience = experience;
        this.salary = salary;
        this.violations = violations;
    }

    public static int create(String firstName, String lastName, Integer divisionId, Integer positionId, Blob photo) throws Exception {
        InputStream inputStream = photo != null ? photo.getBinaryStream() : null;
        String sql = "INSERT INTO staff (firstname, lastname, division_id, position_id, photo) values (?, ?, ?, ?, ?)";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setInt(3, divisionId);
        ps.setInt(4, positionId);
        ps.setBlob(5, inputStream);
        if (ps.executeUpdate() > 0) {
            connect.close();
            return 1;
        }
        connect.close();
        return 0;
    }

    public static int update(int id, String firstName, String lastName, Integer divisionId, Integer positionId, Blob photo) throws Exception {
        InputStream inputStream = photo.getBinaryStream();
        String sql = "UPDATE staff SET firstname = ?, lastname = ?, division_id = ?, position_id = ?, photo = ? WHERE id = ?";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setInt(3, divisionId);
        ps.setInt(4, positionId);
        ps.setBlob(5, inputStream);
        ps.setInt(6, id);
        if (ps.executeUpdate() > 0) {
            connect.close();
            return 1;
        }
        connect.close();
        return 0;
    }

    public static int delete(int id) {
        String unlinkCardSql = "UPDATE cards SET staff_id = NULL, is_active = 0 WHERE staff_id = " + id;
        String dropStaffSql = "DELETE FROM staff WHERE id = " + id;
        Database db = new Database();
        try {
            db.update(unlinkCardSql);
            db.update(dropStaffSql);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
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

    public HashMap<Integer, String> getViolations() {
        return violations;
    }

    public void setViolations(HashMap<Integer, String> violations) {
        this.violations = violations;
    }

}
