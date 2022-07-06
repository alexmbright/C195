package model;

/**
 * This class defines the Customer object.
 *
 * @author Alex Bright
 */
public class Customer {

    private int id;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private Division division;
    private Country country;

    /**
     * Constructor for the Customer object.
     *
     * @param id customer ID
     * @param name customer name
     * @param address customer address
     * @param postal customer postal code
     * @param phone customer phone number
     * @param division customer division
     * @param country customer country
     */
    public Customer(int id, String name, String address, String postal, String phone, Division division, Country country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.division = division;
        this.country = country;
    }

    /**
     * Returns the ID of the customer.
     *
     * @return customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the customer.
     *
     * @param id customer ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the customer.
     *
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the customer.
     *
     * @return customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the postal code of the customer.
     *
     * @return customer postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Sets the postal code of the customer.
     *
     * @param postal customer postal code
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * Returns the phone number of the customer.
     *
     * @return customer phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone customer phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the Division instance of the customer.
     *
     * @return Division instance
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Sets the Division instance of the customer.
     *
     * @param division Division instance
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Returns the Country instance of the customer.
     *
     * @return Country instance
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the Country instance of the customer.
     *
     * @param country Country instance
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Overrides <code>Object.toString()</code> method to allow readable Customer object, containing ID and name.
     * This method is used in various ComboBox fields throughout the program.
     *
     * @return readable format of Customer object, containing ID and name.
     */
    @Override
    public String toString() {
        return id + " - " + name;
    }
}
