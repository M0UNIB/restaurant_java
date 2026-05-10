package dao;

import model.Facture;
import util.DatabaseConnection;

import java.sql.*;

public class FactureDAO {

    public Facture findByCommande(int commandeId) {
        String sql = "SELECT * FROM Facture WHERE commande_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return new Facture(rs.getInt("id"), rs.getInt("commande_id"),
                        rs.getDouble("montant_total"), rs.getTimestamp("date_facture"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void insert(Facture f) {
        String sql = "INSERT INTO Facture (commande_id, montant_total) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, f.getCommandeId());
            ps.setDouble(2, f.getMontantTotal());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) f.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
