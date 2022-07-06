package model;

/**
 * This class defines the User object.
 *
 * @author Alex Bright
 */
public class User {

    private int id;
    private String username;
    private String password;

    /**
     * Constructor for User object.
     *
     * @param id user ID
     * @param username username
     * @param password password
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns ID of user.
     *
     * @return user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets ID of user.
     *
     * @param id user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns username of user.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username of user.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns password of user.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password of user.
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Overrides <code>Object.toString()</code> method to allow readable User object, containing ID and username.
     * This method is used in various ComboBox fields throughout the program.
     *
     * @return readable format of User object, containing ID and username.
     */
    @Override
    public String toString() {
        return id + " - " + username;
    }
}
