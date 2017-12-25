package javalabs.classes;

import javalabs.libraries.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class Division extends Workspace{
    public Division(Integer id, String name, String desc){
        super(id, name, desc);
    }

    public static int create(String name, String desc) throws Exception{
        String sql = "INSERT INTO divisions (division_name, description) VALUES (?, ?)";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, desc);
        if(ps.executeUpdate() > 0){
            connect.close();
            return -1;
        }
        connect.close();
        return 0;
    }

    public static int update(Integer id, String name, String desc) throws Exception{
        String sql = "UPDATE divisions SET division_name = ?, description = ? WHERE id = ?";
        Connection connect = new Database().unsafeGetConnection();
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, desc);
        ps.setInt(3, id);
        if(ps.executeUpdate() > 0){
            connect.close();
            return -1;
        }
        connect.close();
        return 0;
    }

    public static int isUnique(String name){
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT id FROM divisions WHERE division_name = '" + name + "'"
        );
        if(result.size() > 0){
            Object[] row = result.get(0);
            return (int)row[0];
        }
        return 0;
    }

    public static String checkUse(int id){
        Database example = new Database();
        List<Object[]> result = example.query(
                "SELECT firstname, lastname FROM staff WHERE division_id = " + id
        );
        int size = result.size();
        if(size == 0){
            return "";
        }
        String list = "";
        for(int i = 0; i < size; i++){
            Object[] row = result.get(i);
            list += row[0] + " " + row[1] + "\n";
        }
        return list;
    }

    public static int delete(int id){
        String sql = "DELETE FROM divisions WHERE id = " + id;
        Database db = new Database();
        try{
            db.update(sql);
            return 0;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
