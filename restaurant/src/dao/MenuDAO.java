package dao;

import model.Menu;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<Menu> findAll() {
        List<Menu> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Menu")) {
            while (rs.next())
                list.add(new Menu(rs.getInt("id"), rs.getString("nom"), rs.getString("description")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void insert(Menu m) {
        String sql = "INSERT INTO Menu (nom, description) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) m.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Menu m) {
        String sql = "UPDATE Menu SET nom=?, description=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDescription());
            ps.setInt(3, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Menu WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
