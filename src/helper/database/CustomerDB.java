package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDB {

    public static ObservableList<Customer> getAll() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            String q = "select cu.*, d.division, c.country from customers cu " +
                    "left join first_level_divisions d on cu.division_id = d.division_id " +
                    "left join countries c on d.country_id = c.country_id " +
                    "order by cu.customer_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("customer_id");
                String name = res.getString("customer_name");
                String address = res.getString("address");
                String postal = res.getString("postal_code");
                String phone = res.getString("phone");
                String division = res.getString("division");
                String country = res.getString("country");

                customers.add(new Customer(id, name, address, postal, phone, division, country));
            }
        } catch (SQLException e) {
            System.out.println("customer getAll() error: " + e.getMessage());
        }
        return customers;
    }

    public static Customer getCustomer(int id) {
        try {
            String q = "select cu.*, d.division, c.country from customers cu " +
                    "left join first_level_divisions d on cu.division_id = d.division_id " +
                    "left join countries c on d.country_id = c.country_id " +
                    "where cu.customer_id = ? " +
                    "order by cu.customer_id";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                String name = res.getString("customer_name");
                String address = res.getString("address");
                String postal = res.getString("postal_code");
                String phone = res.getString("phone");
                String division = res.getString("division");
                String country = res.getString("country");

                return new Customer(id, name, address, postal, phone, division, country);
            }
        } catch (SQLException e) {
            System.out.println("getCustomer() error: " + e.getMessage());
        }
        return null;
    }

    public static int add(String name, String address, String postal, String phone, int division) {
        try {
            String q = "insert into customers (customer_name, address, postal_code, phone, create_date, " +
                    "created_by, last_update, last_updated_by, division_id) " +
                    "values (?, ?, ?, ?, now(), ?, now(), ?, ?)";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, phone);
            ps.setString(5, UserDB.getCurrentUser().getUsername());
            ps.setString(6, UserDB.getCurrentUser().getUsername());
            ps.setInt(7, division);

            if (ps.executeUpdate() > 0) {
                ps = DatabaseConnection.getConnection().prepareStatement("select last_insert_id() from customers");
                ResultSet res = ps.executeQuery();
                if (res.next()) return res.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("customer add() error: " + e.getMessage());
        }
        return -1;
    }

    public static int update(int id, String name, String address, String postal, String phone, int division) {
        try {
            String q = "update customers set customer_name = ?, address = ?, postal_code = ?, phone = ?, " +
                    "last_update = now(), last_updated_by = ?, division_id = ? " +
                    "where customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, phone);
            ps.setString(5, UserDB.getCurrentUser().getUsername());
            ps.setInt(6, division);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("customer update() error: " + e.getMessage());
        }
        return -1;
    }

    public static int delete(int id) {
        try {
            String q = "delete from customers where customer_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("customer delete() error: " + e.getMessage());
        }
        return -1;
    }

}
