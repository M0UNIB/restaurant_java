package view;

import controller.AuthController;
import model.Utilisateur;
import model.TypeUtilisateur;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final JTextField     usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton        loginButton   = new JButton("Se connecter");
    private final JLabel         errorLabel    = new JLabel(" ");
    private final AuthController authController = new AuthController();

    public LoginView() {
        setTitle("Restaurant — Connexion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill   = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Système de gestion du restaurant", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        c.gridwidth = 2; c.gridx = 0; c.gridy = 0;
        panel.add(title, c);

        // Fields
        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; panel.add(new JLabel("Nom d'utilisateur :"), c);
        c.gridx = 1; panel.add(usernameField, c);

        c.gridy = 2; c.gridx = 0; panel.add(new JLabel("Mot de passe :"), c);
        c.gridx = 1; panel.add(passwordField, c);

        // Error label
        errorLabel.setForeground(Color.RED);
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        panel.add(errorLabel, c);

        // Button
        c.gridy = 4;
        panel.add(loginButton, c);

        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        Utilisateur user = authController.login(username, password);
        if (user == null) {
            errorLabel.setText("Identifiants incorrects.");
        } else {
            dispose();
            openDashboard(user);
        }
    }

    private void openDashboard(Utilisateur user) {
        if (TypeUtilisateur.CUISINIER.equals(user.getType())) {
            new CuisinierView(user);
        } else if (TypeUtilisateur.SERVEUR.equals(user.getType())) {
            new ServeurView(user);
        } else {
            new ClientView(user);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}
