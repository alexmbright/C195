package helper.database;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {

    public static boolean passwordMatches(String username, String password) {
        try {
            String q = "select * from users where user_name = ? and password = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet res = ps.executeQuery();
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User getUser(int id) {
        try {
            String q = "select * from users where user_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                String name = res.getString("user_name");
                String pass = res.getString("password");

                return new User(id, name, pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUser(String username) {
        try {
            String q = "select * from users where user_name = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, username);

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                int id = res.getInt("user_id");
                String pass = res.getString("password");

                return new User(id, username, pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
