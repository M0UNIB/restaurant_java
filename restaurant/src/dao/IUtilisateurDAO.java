package dao;

import model.Utilisateur;
import java.util.List;

public interface IUtilisateurDAO {
    Utilisateur findByUsernameAndPassword(String username, String password);
    Utilisateur findById(int id);
    List<Utilisateur> findAll();
    void insert(Utilisateur u);
    void update(Utilisateur u);
    void delete(int id);
}
