package dao;

import model.Plats;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatsDAO {

    public List<Plats> findAll() {
        List<Plats> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Plats")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Plats> findByMenu(int menuId) {
        List<Plats> list = new ArrayList<>();
        String sql = "SELECT * FROM Plats WHERE menu_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, menuId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void insert(Plats p) {
        String sql = "INSERT INTO Plats (nom, description, prix, menu_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getMenuId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Plats p) {
        String sql = "UPDATE Plats SET nom=?, description=?, prix=?, menu_id=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getMenuId());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Plats WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Plats map(ResultSet rs) throws SQLException {
        return new Plats(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            rs.getInt("menu_id")
        );
    }
}
