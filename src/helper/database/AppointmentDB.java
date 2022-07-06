package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/**
 * This class makes database calls for appointment operations.
 *
 * @author Alex Bright
 */
public class AppointmentDB {

    /**
     * Calls to database and returns all appointments.
     *
     * @return observable list of all appointments
     */
    public static ObservableList<Appointment> getAll() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, co.contact_name from appointments a " +
                    "left join contacts co on a.contact_id = co.contact_id " +
                    "order by a.appointment_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("appointment_id");
                String title = res.getString("title");
                String desc = res.getString("description");
                String location = res.getString("location");
                String type = res.getString("type");
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();
                int customer = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                appts.add(new Appointment(id, title, desc, location, type, start, end, customer, userId, contactId, contactName));
            }
        } catch (SQLException e) {
            System.out.println("appointment getAll() error: " + e.getMessage());
        }
        return appts;
    }

    /**
     * Calls to database and returns appointments for the current month.
     *
     * @return observable list of appointments for this month
     */
    public static ObservableList<Appointment> getThisMonth() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, co.contact_name from appointments a " +
                    "left join contacts co on a.contact_id = co.contact_id " +
                    "where month(a.start) = month(now()) and year(a.start) = year(now()) " +
                    "order by a.appointment_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("appointment_id");
                String title = res.getString("title");
                String desc = res.getString("description");
                String location = res.getString("location");
                String type = res.getString("type");
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();
                int customer = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                Appointment a = new Appointment(id, title, desc, location, type, start, end, customer, userId, contactId, contactName);

                appts.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getThisMonth() error: " + e.getMessage());
        }
        return appts;
    }

    /**
     * Calls to database and returns appointments for the current week.
     *
     * @return observable list of appointments for this week
     */
    public static ObservableList<Appointment> getThisWeek() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, co.contact_name from appointments a " +
                    "left join contacts co on a.contact_id = co.contact_id " +
                    "where yearweek(a.start) = yearweek(now()) " +
                    "order by a.appointment_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("appointment_id");
                String title = res.getString("title");
                String desc = res.getString("description");
                String location = res.getString("location");
                String type = res.getString("type");
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();
                int customer = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                Appointment a = new Appointment(id, title, desc, location, type, start, end, customer, userId, contactId, contactName);

                appts.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getThisWeek() error: " + e.getMessage());
        }
        return appts;
    }

    /**
     * Calls to database and returns appointments connected to desired contact.
     *
     * @param contactId id of selected contact
     * @return observable list of appointments for contact
     */
    public static ObservableList<Appointment> getByContact(int contactId) {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, co.contact_name from appointments a " +
                    "left join contacts co on a.contact_id = co.contact_id " +
                    "where a.contact_id = ? " +
                    "order by a.appointment_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, contactId);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("appointment_id");
                String title = res.getString("title");
                String desc = res.getString("description");
                String location = res.getString("location");
                String type = res.getString("type");
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();
                int customer = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                String contactName = res.getString("contact_name");

                appts.add(new Appointment(id, title, desc, location, type, start, end, customer, userId, contactId, contactName));
            }
        } catch (SQLException e) {
            System.out.println("appointment getByContact() error: " + e.getMessage());
        }
        return appts;
    }

    /**
     * Returns list of appointments starting within the next 15 minutes.
     * This method iterates through all appointments and determines if they are upcoming.
     * If true, adds appointment to returned list.
     *
     * @return observable list of upcoming appointments
     */
    public static ObservableList<Appointment> getUpcoming() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        ObservableList<Appointment> all = getAll();
        if (all.size() == 0) return appts;
        LocalDateTime now = LocalDateTime.now();
        for (Appointment a : all) {
            LocalDateTime start = a.getStart();
            if (start.isAfter(now) && (start.isBefore(now.plusMinutes(15)) || start.isEqual(now.plusMinutes(15))))
                appts.add(a);
        }
        return appts;
    }

    /**
     * Calls to database and returns list of all appointment types.
     * The database call returns only distinct values of type.
     *
     * @return observable list of all appointment types
     */
    public static ObservableList<String> getTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        try {
            String q = "select distinct type from appointments";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                String type = res.getString("type");
                types.add(type);
            }
        } catch (SQLException e) {
            System.out.println("getTypes() error: " + e.getMessage());
        }
        return types;
    }

    /**
     * Given type and month, returns count of matching appointments.
     *
     * @param type selected type
     * @param month selected month
     * @return count of appointments of type during month
     */
    public static int countByTypeAndMonth(String type, Month month) {
        int count = 0;
        try {
            String q = "select count(appointment_id) from appointments where type = ? and (month(start) = ? or month(end) = ?)";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, type);
            ps.setInt(2, month.getValue());
            ps.setInt(3, month.getValue());

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                count = res.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("countByTypeAndMonth() error: " + e.getMessage());
        }
        return count;
    }

    /**
     * Given user and customer, returns count of matching appointments.
     *
     * @param user selected user
     * @param customer selected customer
     * @return count of appointments with user and customer
     */
    public static int countByUserAndCustomer(User user, Customer customer) {
        int count = 0;
        try {
            String q = "select count(appointment_id) from appointments where user_id = ? and customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, user.getId());
            ps.setInt(2, customer.getId());

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                count = res.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("countByUserAndCustomer() error: " + e.getMessage());
        }
        return count;
    }

    /**
     * Calls to database and inserts new appointment.
     *
     * @param title appointment title
     * @param desc appointment description
     * @param loc appointment location
     * @param type appointment type
     * @param start appointment start time/date, expressed in local timezone
     * @param end appointment end time/date, expressed in local timezone
     * @param custId customer ID
     * @param userId user ID
     * @param contId contact ID
     * @return new appointment ID if successful, otherwise -1 to indicate failure
     */
    public static int add(String title, String desc, String loc, String type, LocalDateTime start, LocalDateTime end, int custId, int userId, int contId) {
        try {
            String q = "insert into appointments (title, description, location, type, start, end, " +
                    "create_date, created_by, last_update, last_updated_by, customer_id, user_id, contact_id) " +
                    " values (?, ?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?)";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, loc);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setString(7, UserDB.getCurrentUser().getUsername());
            ps.setString(8, UserDB.getCurrentUser().getUsername());
            ps.setInt(9, custId);
            ps.setInt(10, userId);
            ps.setInt(11, contId);

            if (ps.executeUpdate() > 0) {
                ps = DatabaseConnection.getConnection().prepareStatement("select last_insert_id() from appointments");
                ResultSet res = ps.executeQuery();
                if (res.next()) return res.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("appointment add() error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Calls to database and updates existing appointment by ID.
     *
     * @param id existing appointment ID
     * @param title appointment title
     * @param desc appointment description
     * @param loc appointment location
     * @param type appointment type
     * @param start appointment start time/date, expressed in local timezone
     * @param end appointment end time/date, expressed in local timezone
     * @param custId customer ID
     * @param userId user ID
     * @param contId contact ID
     * @return count of rows updated if successful, otherwise -1 to indicate failure
     */
    public static int update(int id, String title, String desc, String loc, String type, LocalDateTime start, LocalDateTime end, int custId, int userId, int contId) {
        try {
            String q = "update appointments set title = ?, description = ?, location = ?, type = ?, start = ?, " +
                    "end = ?, last_update = now(), last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ? " +
                    "where appointment_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setString(3, loc);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setString(7, UserDB.getCurrentUser().getUsername());
            ps.setInt(8, custId);
            ps.setInt(9, userId);
            ps.setInt(10, contId);
            ps.setInt(11, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("appointment update() error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Calls to database and deletes appointment by ID.
     *
     * @param id ID of appointment to delete
     * @return count of rows deleted if successful, otherwise -1 to indicate failure
     */
    public static int delete(int id) {
        try {
            String q = "delete from appointments where appointment_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("appointment delete() error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Determines if customer has existing appointments in the database.
     * This method calls to the database using the given customer ID, then returns if there were any results.
     *
     * @param id existing customer ID
     * @return true if customer has appointments, otherwise false
     */
    public static boolean hasCustomer(int id) {
        try {
            String q = "select * from appointments where customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            ResultSet res = ps.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.out.println("hasCustomer() error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Determines if given start and end dates/times overlap with existing appointments by customer.
     * This method calls to the database using the given customer ID, then iterates through matching appointments
     * to determine if there are any overlapping dates/times.
     *
     * @param customerId existing customer ID
     * @param newStart desired start date/time, expressed in local timezone
     * @param newEnd desired end date/time, expressed in local timezone
     * @return true if given start and end dates/times overlap with existing customer appointments, otherwise false
     */
    public static boolean overlaps(int customerId, LocalDateTime newStart, LocalDateTime newEnd) {
        try {
            String q = "select start, end from appointments " +
                    "where customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, customerId);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();

                if (start.isBefore(newEnd) && newStart.isBefore(end)) return true;
            }
        } catch (SQLException e) {
            System.out.println("overlaps() error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Determines if given start and end dates/times overlap with existing appointments by customer, excluding
     * appointment with given ID.
     * This method calls to the database using the given customer ID, then iterates through matching appointments
     * to determine if there are any overlapping dates/times.
     *
     * @param customerId existing customer ID
     * @param newStart desired start date/time, expressed in local timezone
     * @param newEnd desired end date/time, expressed in local timezone
     * @param apptId existing appointment ID to exclude from comparison
     * @return true if given start and end dates/times overlap with existing customer appointments, otherwise false
     */
    public static boolean overlaps(int customerId, LocalDateTime newStart, LocalDateTime newEnd, int apptId) {
        try {
            String q = "select appointment_id, start, end from appointments " +
                    "where customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, customerId);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                if (res.getInt("appointment_id") == apptId) continue;
                LocalDateTime start = res.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = res.getTimestamp("end").toLocalDateTime();

                if (start.isBefore(newEnd) && newStart.isBefore(end)) return true;
            }
        } catch (SQLException e) {
            System.out.println("overlaps() error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns list of times, as strings, during business hours, expressed in local timezone.
     *
     * @return observable list of times in local timezone during business hours, expressed as strings
     */
    public static ObservableList<String> getValidTimes() {
        ObservableList<String> times = FXCollections.observableArrayList();

        ZoneId est = ZoneId.of("US/Eastern");
        ZoneId local = ZoneId.systemDefault();

        LocalDateTime open = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));
        LocalDateTime close = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));
        open = open.atZone(est).withZoneSameInstant(local).toLocalDateTime();
        close = close.atZone(est).withZoneSameInstant(local).toLocalDateTime();

        for (int i = open.getHour(); i < close.getHour(); i++) {
            for (int j = 0; j < 4; j++) {
                times.add(String.format("%02d", i) + ":" + String.format("%02d", j * 15));
            }
        }
        times.add(String.format("%02d", close.getHour()) + ":00");

        return times;
    }

}
