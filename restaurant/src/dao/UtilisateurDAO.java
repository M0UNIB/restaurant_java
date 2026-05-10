package dao;

import model.Utilisateur;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements IUtilisateurDAO {

    @Override
    public Utilisateur findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM Utilisateur WHERE username = ? AND password = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void insert(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (nom, prenom, username, password, type) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getType());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) u.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Utilisateur u) {
        String sql = "UPDATE Utilisateur SET nom=?, prenom=?, username=?, password=?, type=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getType());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Utilisateur map(ResultSet rs) throws SQLException {
        return new Utilisateur(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("type")
        );
    }
}
