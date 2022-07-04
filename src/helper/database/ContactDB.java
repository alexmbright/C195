package helper.database;

import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDB {

    public static Contact getContact(int id) {
        try {
            String q = "select * from contacts where contact_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                String name = res.getString("customer_name");
                String email = res.getString("email");

                return new Contact(id, name, email);
            }
        } catch (SQLException e) {
            System.out.println("getContact() error: " + e.getMessage());
        }
        return null;
    }

}
