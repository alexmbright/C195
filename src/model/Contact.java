package model;

/**
 * This class defines the Contact object.
 *
 * @author Alex Bright
 */
public class Contact {

    private int id;
    private String name;
    private String email;

    /**
     * Constructor for the Contact object.
     *
     * @param id contact ID
     * @param name contact name
     * @param email contact email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns ID of contact.
     *
     * @return contact ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets ID of contact.
     *
     * @param id contact ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns name of contact.
     *
     * @return contact name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of contact.
     *
     * @param name contact name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns email address of contact.
     *
     * @return contact email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address of contact.
     *
     * @param email contact email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Overrides <code>Object.toString()</code> method to allow readable Contact object, containing ID and name.
     * This method is used in various ComboBox fields throughout the program.
     *
     * @return readable format of Contact object, containing ID and name.
     */
    @Override
    public String toString() {
        return id + " - " + name;
    }
}
