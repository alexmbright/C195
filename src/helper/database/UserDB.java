package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class makes database calls for user operations.
 *
 * @author Alex Bright
 */
public class UserDB {

    private static User currentUser;

    /**
     * Calls to database and returns list of all customers.
     *
     * @return observable list of all customers
     */
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

    /**
     * Calls to database and determines if user exists with matching username and password.
     *
     * @param username existing username
     * @param password attempted password
     * @return true if existing user has matching username and password, otherwise false
     */
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

    /**
     * Calls to database and returns matching instance of User.
     *
     * @param id ID of user to find
     * @return matching instance of User if successful, otherwise null
     */
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

    /**
     * Calls to database and returns matching instance of User.
     *
     * @param username username of user to find
     * @return matching instance of User if successful, otherwise null
     */
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

    /**
     * Sets currently logged in user to given instance of User.
     *
     * @param user instance of User to set as logged in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Returns instance of User currently logged in.
     *
     * @return instance of User currently logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

}
