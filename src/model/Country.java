package model;

/**
 * This class defines the Country object.
 *
 * @author Alex Bright
 */
public class Country {

    private int id;
    private String country;

    /**
     * Constructor for the Country object.
     *
     * @param id country ID
     * @param country country name
     */
    public Country(int id, String country) {
        this.id = id;
        this.country = country;
    }

    /**
     * Returns ID of country.
     *
     * @return country ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets ID of country.
     *
     * @param id country ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns name of country.
     *
     * @return country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets name of country.
     *
     * @param country country name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Overrides <code>Object.toString()</code> method to allow readable Country object, containing ID and name.
     * This method is used in various ComboBox fields throughout the program.
     *
     * @return readable format of Country object, containing ID and name.
     */
    @Override
    public String toString() {
        return id + " - " + country;
    }
}
