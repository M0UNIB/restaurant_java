package controller;

import dao.UtilisateurDAO;
import model.Utilisateur;

public class AuthController {
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public Utilisateur login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank())
            return null;
        return utilisateurDAO.findByUsernameAndPassword(username, password);
    }
}
