package model;

/**
 * This class defines the Division object.
 *
 * @author Alex Bright
 */
public class Division {

    private int id;
    private String division;
    private int countryId;

    /**
     * Constructor for Division object.
     *
     * @param id division ID
     * @param division division name
     * @param countryId country ID
     */
    public Division(int id, String division, int countryId) {
        this.id = id;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Returns ID of division.
     *
     * @return division ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets ID of division.
     *
     * @param id division ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns name of division.
     *
     * @return division name
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets name of division.
     *
     * @param division division name
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Returns ID of country assigned to division.
     *
     * @return ID of assigned country
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets ID of country assigned to division.
     *
     * @param countryId ID of assigned country
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Overrides <code>Object.toString()</code> method to allow readable Division object, containing ID and name.
     * This method is used in various ComboBox fields throughout the program.
     *
     * @return readable format of Division object, containing ID and name.
     */
    @Override
    public String toString() {
        return id + " - " + division;
    }
}
