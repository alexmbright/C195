package helper.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDB {

    public static ObservableList<Country> getAll() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        try {
            String q = "select * from countries";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);

            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("country_id");
                String country = res.getString("country");

                countries.add(new Country(id, country));
            }
        } catch (SQLException e) {
            System.out.println("country getAll() error: " + e.getMessage());
        }
        return countries;
    }

    public static Country getByDivision(int id) {
        try {
            String q = "select c.* from countries c " +
                    "inner join first_level_divisions d on c.country_id = d.country_id " +
                    "where d.division_id = ?";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(q);
            ps.setInt(1, id);

            ResultSet res = ps.executeQuery();
            if (res.next()) {
                int countryId = res.getInt("country_id");
                String country = res.getString("country");

                return new Country(countryId, country);
            }
        } catch (SQLException e) {
            System.out.println("country getByDivision() error: " + e.getMessage());
        }
        return null;
    }

}