package dao;

import model.Commande;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    public Commande findById(int id) {
        String sql = "SELECT * FROM Commande WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Commande> findAll() {
        List<Commande> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getInstance().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Commande ORDER BY date_commande DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Commande> findByEtat(String etat) {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE etat = ? ORDER BY date_commande";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, etat);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Commande> findByUtilisateur(int utilisateurId) {
        List<Commande> list = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE utilisateur_id = ? ORDER BY date_commande DESC";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void insert(Commande c) {
        String sql = "INSERT INTO Commande (utilisateur_id, etat) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getUtilisateurId());
            ps.setString(2, c.getEtat());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateEtat(int commandeId, String etat) {
        String sql = "UPDATE Commande SET etat = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setString(1, etat);
            ps.setInt(2, commandeId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Commande map(ResultSet rs) throws SQLException {
        return new Commande(
            rs.getInt("id"),
            rs.getInt("utilisateur_id"),
            rs.getTimestamp("date_commande"),
            rs.getString("etat")
        );
    }
}
