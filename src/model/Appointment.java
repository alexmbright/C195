package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class defines the Appointment object.
 *
 * @author Alex Bright
 */
public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;

    /**
     * Constructor for the Appointment object.
     *
     * @param id appointment ID
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param start appointment start date/time, expressed in local timezone
     * @param end appointment end date/time, expressed in local timezone
     * @param customerId customer ID
     * @param userId user ID
     * @param contactId contact ID
     * @param contactName contact name
     */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId, String contactName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Returns ID of appointment.
     *
     * @return appointment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets ID of appointment.
     *
     * @param id appointment ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns title of appointment.
     *
     * @return appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of appointment.
     *
     * @param title appointment title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns description of appointment.
     *
     * @return appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of appointment.
     *
     * @param description appointment title
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns location of appointment.
     *
     * @return appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location of appointment.
     *
     * @param location appointment location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns type of appointment.
     *
     * @return appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type of appointment.
     *
     * @param type appointment type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns start date/time of appointment, expressed in local timezone.
     *
     * @return appointment start date/time, expressed in local timezone
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Sets start date/time of appointment, expressed in local timezone.
     *
     * @param start appointment start date/time, expressed in local timezone
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Returns end date/time of appointment, expressed in local timezone.
     *
     * @return appointment end date/time, expressed in local timezone
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Sets end date/time of appointment, expressed in local timezone.
     *
     * @param end appointment end date/time, expressed in local timezone
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Returns ID of customer assigned to appointment.
     *
     * @return ID of assigned customer
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets ID of customer assigned to appointment.
     *
     * @param customerId customer ID to assign
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns ID of user assigned to appointment.
     *
     * @return ID of assigned user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets ID of user assigned to appointment.
     *
     * @param userId user ID to assign
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns ID of contact assigned to appointment.
     *
     * @return ID of assigned contact
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets ID of contact assigned to appointment.
     *
     * @param contactId contact ID to assign
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns name of contact assigned to appointment.
     *
     * @return name of assigned contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets name of contact assigned to appointment.
     *
     * @param contactName contact name to assign
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Returns formatted string of start date/time, expressed in local timezone.
     *
     * @return formatted string of start date/time, expressed in local timezone
     */
    public String getFormattedStart() {
        return start.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }

    /**
     * Returns formatted end of start date/time, expressed in local timezone.
     *
     * @return formatted end of start date/time, expressed in local timezone
     */
    public String getFormattedEnd() {
        return end.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }
}
