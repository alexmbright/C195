package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentDB {

    public static ObservableList<Appointment> getAll() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, c.contact_name from appointments a " +
                    "left join contacts c on a.contact_id = c.contact_id " +
                    "order by appointment_id";
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
                int customerId = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                Appointment a = new Appointment(id, title, desc, location, type, start, end, customerId, userId, contactId, contactName);

                appts.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getAll() error: " + e.getMessage());
        }
        return appts;
    }

    public static ObservableList<Appointment> getMonth() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, c.contact_name from appointments a " +
                    "left join contacts c on a.contact_id = c.contact_id " +
                    "where month(start) = month(now()) and year(start) = year(now()) " +
                    "order by appointment_id";
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
                int customerId = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                Appointment a = new Appointment(id, title, desc, location, type, start, end, customerId, userId, contactId, contactName);

                appts.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getMonth() error: " + e.getMessage());
        }
        return appts;
    }

    public static ObservableList<Appointment> getWeek() {
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            String q = "select a.*, c.contact_name from appointments a " +
                    "left join contacts c on a.contact_id = c.contact_id " +
                    "where yearweek(start) = yearweek(now()) " +
                    "order by appointment_id";
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
                int customerId = res.getInt("customer_id");
                int userId = res.getInt("user_id");
                int contactId = res.getInt("contact_id");
                String contactName = res.getString("contact_name");

                Appointment a = new Appointment(id, title, desc, location, type, start, end, customerId, userId, contactId, contactName);

                appts.add(a);
            }
        } catch (SQLException e) {
            System.out.println("getWeek() error: " + e.getMessage());
        }
        return appts;
    }

    public static int add(String title, String desc, String loc, String type, LocalDateTime start, LocalDateTime end, int custId, int contId) {
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
            ps.setInt(10, UserDB.getCurrentUser().getId());
            ps.setInt(11, contId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("appointment add() error: " + e.getMessage());
        }
        return -1;
    }

    public static int update(int id, String title, String desc, String loc, String type, LocalDateTime start, LocalDateTime end, int custId, int contId) {
        try {
            String q = "update appointments set title = ?, description = ?, location = ?, type = ?, start = ?, " +
                    "end = ?, last_update = now(), last_updated_by = ?, customer_id = ?, contact_id = ? " +
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
            ps.setInt(9, contId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("appointment update() error: " + e.getMessage());
        }
        return -1;
    }

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

}
