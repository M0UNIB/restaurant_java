package dao;

import model.LigneCommande;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO {

    public List<LigneCommande> findByCommande(int commandeId) {
        List<LigneCommande> list = new ArrayList<>();
        String sql = "SELECT * FROM LigneCommande WHERE commande_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void insert(LigneCommande lc) {
        String sql = "INSERT INTO LigneCommande (commande_id, plat_id, quantite, prix_unitaire) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lc.getCommandeId());
            ps.setInt(2, lc.getPlatId());
            ps.setInt(3, lc.getQuantite());
            ps.setDouble(4, lc.getPrixUnitaire());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) lc.setId(keys.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteByCommande(int commandeId) {
        String sql = "DELETE FROM LigneCommande WHERE commande_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private LigneCommande map(ResultSet rs) throws SQLException {
        return new LigneCommande(
            rs.getInt("id"),
            rs.getInt("commande_id"),
            rs.getInt("plat_id"),
            rs.getInt("quantite"),
            rs.getDouble("prix_unitaire")
        );
    }
}
