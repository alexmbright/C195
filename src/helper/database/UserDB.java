package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {

    private static User currentUser;

    public static ObservableList<User> getAll() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String q = "select * from users";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("user_id");
                String username = res.getString("user_name");
                String password = res.getString("password");

                users.add(new User(id, username, password));
            }
        } catch (SQLException e) {
            System.out.println("user getAll() error: " + e.getMessage());
        }
        return users;
    }

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

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

}
